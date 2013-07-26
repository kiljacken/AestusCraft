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
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import dk.kiljacken.aestuscraft.api.heat.HeatNetworkImpl;
import dk.kiljacken.aestuscraft.api.heat.IHeatConductor;
import dk.kiljacken.aestuscraft.api.heat.IHeatConsumer;
import dk.kiljacken.aestuscraft.api.heat.IHeatNetwork;
import dk.kiljacken.aestuscraft.api.heat.IHeatProducer;
import dk.kiljacken.aestuscraft.core.blocks.BlockInfo;
import dk.kiljacken.aestuscraft.library.nbt.NBTUtil.NBTValue;

public class TileHeatConductor extends BaseTile implements IHeatConductor {
    private IHeatNetwork m_Network;
    private List<IHeatConsumer> m_ConnectedConsumers;
    private List<IHeatProducer> m_ConnectedProducers;
    private List<IHeatConductor> m_ConnectedConductors;
    private boolean m_ShouldUpdate;

    @NBTValue(name = "connectedSides")
    private int m_ConnectedSides;

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
        m_ConnectedSides = 0;
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote && m_ShouldUpdate) {
            m_ConnectedConsumers.clear();
            m_ConnectedProducers.clear();
            m_ConnectedConductors.clear();
            m_ConnectedSides = 0;

            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                TileEntity neighbour = worldObj.getBlockTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);

                if (neighbour != null) {
                    if (neighbour instanceof IHeatConsumer) {
                        m_ConnectedConsumers.add((IHeatConsumer) neighbour);
                        m_ConnectedSides |= 1 << direction.ordinal();
                    } else if (neighbour instanceof IHeatProducer) {
                        m_ConnectedProducers.add((IHeatProducer) neighbour);

                        ((IHeatProducer) neighbour).setNetwork(getNetwork());
                        m_ConnectedSides |= 1 << direction.ordinal();
                    } else if (neighbour instanceof IHeatConductor) {
                        m_ConnectedConductors.add((IHeatConductor) neighbour);
                        m_ConnectedSides |= 1 << direction.ordinal();
                    }
                }
            }

            for (IHeatConductor conductor : getConnectedConductors()) {
                getNetwork().merge(conductor.getNetwork());
            }

            getNetwork().refresh();

            worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlockInfo.BLOCK_HEAT_CONDUCTOR_ID, 0, m_ConnectedSides);

            m_ShouldUpdate = false;
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int data) {
        if (worldObj.isRemote) {
            if (id == 0) {
                m_ConnectedSides = data;
            }
        }

        return true;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        boolean yConnection = (m_ConnectedSides & (1 | 2)) != 0;
        boolean zConnection = (m_ConnectedSides & (4 | 8)) != 0;
        boolean xConnection = (m_ConnectedSides & (16 | 32)) != 0;

        float minY = (m_ConnectedSides & 1) != 0 ? 0.0f : zConnection | xConnection ? 0.25f : 0.3125f;
        float maxY = (m_ConnectedSides & 2) != 0 ? 1.0f : zConnection | xConnection ? 0.75f : 0.6875f;

        float minZ = (m_ConnectedSides & 4) != 0 ? 0.0f : yConnection | xConnection ? 0.25f : 0.3125f;
        float maxZ = (m_ConnectedSides & 8) != 0 ? 1.0f : yConnection | xConnection ? 0.75f : 0.6875f;

        float minX = (m_ConnectedSides & 16) != 0 ? 0.0f : yConnection | zConnection ? 0.25f : 0.3125f;
        float maxX = (m_ConnectedSides & 32) != 0 ? 1.0f : yConnection | zConnection ? 0.75f : 0.6875f;

        return AxisAlignedBB.getAABBPool().getAABB(xCoord + minX, yCoord + minY, zCoord + minZ, xCoord + maxX, yCoord + maxY, zCoord + maxZ);
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

    public int getConnectedSides() {
        return m_ConnectedSides;
    }
}
