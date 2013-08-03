/**
 * AestusCraft
 * 
 * TileFrictionHeater.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.tiles;

import dk.kiljacken.aestuscraft.api.info.BlockInfo;
import dk.kiljacken.aestuscraft.library.nbt.NBTUtil.NBTValue;
import dk.kiljacken.aestuscraft.library.nbt.handlers.BooleanNBTHandler;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;

public class TileFrictionHeater extends HeatProducerBaseTile implements IPowerReceptor {
    public static float HEAT_PER_MJ = 1.0f;
    public static float HEAT_TRANSFER_RATE = 8.0f;

    @NBTValue(name = "active", handler = BooleanNBTHandler.class)
    private boolean m_Active;
    private PowerHandler m_PowerHandler;
    private int m_UpdateTicks;
    private boolean m_RecievedEnergy;

    public TileFrictionHeater() {
        super(1600);

        m_Active = false;
        m_PowerHandler = new PowerHandler(this, Type.MACHINE);
        m_PowerHandler.configure(100, 200, 0, 1600);
        m_PowerHandler.configurePowerPerdition(1, 1);
        m_UpdateTicks = 100;
        m_RecievedEnergy = false;
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            float energy = m_PowerHandler.useEnergy(0.0f, HEAT_TRANSFER_RATE / HEAT_PER_MJ, true);
            m_RecievedEnergy |= energy > 0;
            float heatSupplied = Math.min(getMaxHeatLevel() - getHeatLevel(), energy * HEAT_PER_MJ);

            setHeatLevel(getHeatLevel() + heatSupplied);
            m_PowerHandler.addEnergy(energy - heatSupplied / HEAT_PER_MJ);

            if (m_UpdateTicks == 0 && getHeatLevel() > 0.0f && !m_Active) {
                m_Active = true;
            } else if (m_UpdateTicks == 0 && m_Active) {
                m_Active = false;
            }

            if (m_UpdateTicks == 0) {
                m_Active = m_RecievedEnergy;

                worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlockInfo.FRICTION_HEATER_ID, 0, m_Active ? 1 : 0);

                m_UpdateTicks = 100;
                m_RecievedEnergy = false;
            }

            if (getNetwork() != null) {
                setHeatLevel(getHeatLevel() - getNetwork().supplyHeat(Math.min(getHeatLevel(), HEAT_TRANSFER_RATE)));
            }

            m_UpdateTicks--;
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int data) {
        if (worldObj.isRemote) {
            if (id == 0) {
                m_Active = data != 0;
                worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
            }
        }

        return true;
    }

    @Override
    public PowerReceiver getPowerReceiver(ForgeDirection side) {
        return m_PowerHandler.getPowerReceiver();
    }

    @Override
    public void doWork(PowerHandler workProvider) {
    }

    @Override
    public World getWorld() {
        return worldObj;
    }

    public boolean isActive() {
        return m_Active;
    }
}
