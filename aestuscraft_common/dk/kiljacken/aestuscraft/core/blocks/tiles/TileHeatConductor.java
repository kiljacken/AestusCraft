/**
 * AestusCraft
 * 
 * TileHeatConductor.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.blocks.tiles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import dk.kiljacken.aestuscraft.api.heat.HeatNetworkImpl;
import dk.kiljacken.aestuscraft.api.heat.IHeatConductor;
import dk.kiljacken.aestuscraft.api.heat.IHeatConsumer;
import dk.kiljacken.aestuscraft.api.heat.IHeatNetwork;
import dk.kiljacken.aestuscraft.api.heat.IHeatProducer;

public class TileHeatConductor extends BaseTile implements IHeatConductor {
    private IHeatNetwork m_Network;
    private List<IHeatConsumer> m_ConnectedConsumers;
    private List<IHeatProducer> m_ConnectedProducers;
    private List<IHeatConductor> m_ConnectedConductors;
    private boolean m_ShouldUpdate;

    public TileHeatConductor() {
        m_Network = null;
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            m_Network = new HeatNetworkImpl();
            m_Network.getConnectedConductors().add(this);
        }

        m_ConnectedConsumers = new ArrayList<>(6);
        m_ConnectedProducers = new ArrayList<>(6);
        m_ConnectedConductors = new ArrayList<>(6);
        m_ShouldUpdate = true;
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote && m_ShouldUpdate) {
            m_ConnectedConsumers.clear();
            m_ConnectedProducers.clear();
            m_ConnectedConductors.clear();

            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                TileEntity neighbour = worldObj.getBlockTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);

                if (neighbour != null) {
                    if (neighbour instanceof IHeatConsumer) {
                        m_ConnectedConsumers.add((IHeatConsumer) neighbour);
                    } else if (neighbour instanceof IHeatProducer) {
                        m_ConnectedProducers.add((IHeatProducer) neighbour);

                        ((IHeatProducer) neighbour).setNetwork(getNetwork());
                    } else if (neighbour instanceof IHeatConductor) {
                        m_ConnectedConductors.add((IHeatConductor) neighbour);
                    }
                }
            }

            for (IHeatConductor conductor : getConnectedConductors()) {
                getNetwork().merge(conductor.getNetwork());
            }

            getNetwork().refresh();

            m_ShouldUpdate = false;
        }
    }

    @Override
    public IHeatNetwork getNetwork() {
        return m_Network;
    }

    @Override
    public void setNetwork(IHeatNetwork network) {
        m_Network = network;
    }

    @Override
    public List<IHeatConsumer> getConnectedConsumers() {
        return m_ConnectedConsumers;
    }

    @Override
    public List<IHeatProducer> getConnectedProducers() {
        return m_ConnectedProducers;
    }

    @Override
    public List<IHeatConductor> getConnectedConductors() {
        return m_ConnectedConductors;
    }

    @Override
    public boolean isValid() {
        return !isInvalid();
    }

    public void setShouldUpdate() {
        m_ShouldUpdate = true;
    }
}
