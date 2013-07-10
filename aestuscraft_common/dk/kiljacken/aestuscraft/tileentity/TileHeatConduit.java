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

import net.minecraft.tileentity.TileEntity;
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

    public boolean shouldUpdate = true;

    public TileHeatConduit() {
        m_HeatNetwork = new HeatNetwork();
        m_HeatNetwork.refresh();
        m_HeatNetwork.getHeatConduits().add(this);

        m_ConnectedConduits = new HashSet<>();
        m_ConnectedConsumers = new HashSet<>();
        m_ConnectedProducers = new HashSet<>();
    }

    @Override
    public void updateEntity() {
        if (shouldUpdate) {
            m_ConnectedConduits.clear();
            m_ConnectedConsumers.clear();
            m_ConnectedProducers.clear();

            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                int neighbourX = xCoord + direction.offsetX;
                int neighbourY = yCoord + direction.offsetY;
                int neighbourZ = zCoord + direction.offsetZ;

                if (worldObj.blockExists(neighbourX, neighbourY, neighbourZ) && worldObj.blockHasTileEntity(neighbourX, neighbourY, neighbourZ)) {
                    TileEntity neighbourTile = worldObj.getBlockTileEntity(neighbourX, neighbourY, neighbourZ);

                    if (neighbourTile instanceof IHeatConduit) {
                        m_ConnectedConduits.add((IHeatConduit) neighbourTile);
                    } else if (neighbourTile instanceof IHeatConsumer) {
                        m_ConnectedConsumers.add((IHeatConsumer) neighbourTile);
                    } else if (neighbourTile instanceof IHeatProducer) {
                        m_ConnectedProducers.add((IHeatProducer) neighbourTile);
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
    public Set<IHeatProducer> getConnectedProducers() {
        return m_ConnectedProducers;
    }

}
