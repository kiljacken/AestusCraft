/**
 * AestusCraft
 * 
 * TileHeatConduit.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.tileentity;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import dk.kiljacken.aestuscraft.api.heat.IHeatConduit;
import dk.kiljacken.aestuscraft.api.heat.IHeatConsumer;
import dk.kiljacken.aestuscraft.api.heat.IHeatNetwork;
import dk.kiljacken.aestuscraft.api.heat.IHeatProducer;
import dk.kiljacken.aestuscraft.heat.HeatNetwork;

public class TileHeatConduit extends TileAEC implements IHeatConduit {
    private IHeatNetwork m_HeatNetwork;
    private Set<IHeatConduit> m_ConnectedConduits;
    private Set<IHeatConsumer> m_ConnectedConsumers;
    private Set<IHeatProducer> m_ConnectedProducers;
    private int m_ConnectedSides;

    public boolean shouldUpdate = true;

    public TileHeatConduit() {
        m_HeatNetwork = new HeatNetwork();
        m_HeatNetwork.refresh();
        m_HeatNetwork.getHeatConduits().add(this);

        m_ConnectedConduits = new HashSet<>();
        m_ConnectedConsumers = new HashSet<>();
        m_ConnectedProducers = new HashSet<>();

        m_ConnectedSides = 0;
    }

    @Override
    public void updateEntity() {
        if (shouldUpdate) {
            m_ConnectedConduits.clear();
            m_ConnectedConsumers.clear();
            m_ConnectedProducers.clear();

            m_ConnectedSides = 0;

            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                int neighbourX = xCoord + direction.offsetX;
                int neighbourY = yCoord + direction.offsetY;
                int neighbourZ = zCoord + direction.offsetZ;

                if (worldObj.blockExists(neighbourX, neighbourY, neighbourZ) && worldObj.blockHasTileEntity(neighbourX, neighbourY, neighbourZ)) {
                    TileEntity neighbourTile = worldObj.getBlockTileEntity(neighbourX, neighbourY, neighbourZ);

                    if (neighbourTile instanceof IHeatConduit) {
                        m_ConnectedConduits.add((IHeatConduit) neighbourTile);

                        m_ConnectedSides |= 1 << direction.ordinal();
                    } else if (neighbourTile instanceof IHeatConsumer) {
                        m_ConnectedConsumers.add((IHeatConsumer) neighbourTile);

                        m_ConnectedSides |= 1 << direction.ordinal();
                    } else if (neighbourTile instanceof IHeatProducer) {
                        IHeatProducer heatProducer = (IHeatProducer) neighbourTile;

                        heatProducer.connectToNetwork(m_HeatNetwork);
                        m_ConnectedProducers.add(heatProducer);

                        m_ConnectedSides |= 1 << direction.ordinal();
                    }
                }
            }

            for (IHeatConduit connectedConduit : m_ConnectedConduits) {
                if (m_HeatNetwork != connectedConduit.getNetwork()) {
                    m_HeatNetwork.merge(connectedConduit.getNetwork());
                }
            }

            m_HeatNetwork.refresh();

            shouldUpdate = false;
        }
    }

    @Override
    public IHeatNetwork getNetwork() {
        return m_HeatNetwork;
    }

    @Override
    public void setNetwork(IHeatNetwork network) {
        m_HeatNetwork = network;

        network.getHeatConduits().add(this);
    }

    @Override
    public Set<IHeatConduit> getConnectedConduits() {
        return m_ConnectedConduits;
    }

    @Override
    public Set<IHeatConsumer> getConnectedConsumers() {
        return m_ConnectedConsumers;
    }

    @Override
    public boolean isValid() {
        return !isInvalid();
    }

    @Override
    public Set<IHeatProducer> getConnectedProducers() {
        return m_ConnectedProducers;
    }

    public int getConnectedSides() {
        return m_ConnectedSides;
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
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        shouldUpdate = true;
    }
}
