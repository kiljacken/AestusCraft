/**
 * AestusCraft
 * 
 * HeatNetwork.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.heat;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import dk.kiljacken.aestuscraft.api.heat.IHeatConduit;
import dk.kiljacken.aestuscraft.api.heat.IHeatConsumer;
import dk.kiljacken.aestuscraft.api.heat.IHeatNetwork;
import dk.kiljacken.aestuscraft.api.heat.IHeatProducer;

public class HeatNetwork implements IHeatNetwork {
    private Set<IHeatConsumer> m_HeatConsumers;
    private Set<IHeatProducer> m_HeatProducers;
    private Set<IHeatConduit> m_HeatConduits;

    private Iterator<IHeatConsumer> m_ConsumerIterator;

    public HeatNetwork() {
        m_HeatConsumers = new HashSet<>();
        m_HeatProducers = new HashSet<>();
        m_HeatConduits = new HashSet<>();
    }

    @Override
    public void refresh() {
        m_HeatConsumers.clear();
        m_HeatProducers.clear();

        Iterator<IHeatConduit> iter = m_HeatConduits.iterator();

        while (iter.hasNext()) {
            IHeatConduit conduit = iter.next();

            if (conduit == null) {
                iter.remove();
            } else if (conduit.isInvalid()) {
                iter.remove();
            } else {
                conduit.setNetwork(this);

                m_HeatConsumers.addAll(conduit.getConnectedConsumers());
                m_HeatProducers.addAll(conduit.getConnectedProducers());
            }
        }
    }

    @Override
    public int supplyHeat(int amount) {
        // If consumer iterator doesn't exist or has reached the end, create a
        // new one
        if (m_ConsumerIterator == null || !m_ConsumerIterator.hasNext()) {
            m_ConsumerIterator = m_HeatConsumers.iterator();
        }

        // Find the first consumer accepting heat. Using the iterator allows us
        // to evenly spread heat between consumers
        IHeatConsumer consumer;
        do {
            consumer = m_ConsumerIterator.next();
        } while (!consumer.isAcceptingHeat() && m_ConsumerIterator.hasNext());

        // Make sure consumer accepts heat, and we didn't just reach the end of
        // the iterator
        if (consumer.isAcceptingHeat()) {
            return consumer.supplyHeat(amount);
        } else {
            return 0;
        }
    }

    @Override
    public Set<IHeatConsumer> getHeatConsumers() {
        return m_HeatConsumers;
    }

    @Override
    public Set<IHeatProducer> getHeatProducers() {
        return m_HeatProducers;
    }

    @Override
    public Set<IHeatConduit> getHeatConduits() {
        return m_HeatConduits;
    }

    @Override
    public void merge(IHeatNetwork network) {
        if (network != null && network != this) {
            HeatNetwork newNetwork = new HeatNetwork();
            newNetwork.getHeatConduits().addAll(this.getHeatConduits());
            newNetwork.getHeatConduits().addAll(network.getHeatConduits());
            newNetwork.refresh();
        }
    }

    @Override
    public void split(IHeatConduit conduit) {
        m_HeatConduits.remove(conduit);

        Queue<IHeatConduit> floodQueue = new LinkedList<>();

        while (!m_HeatConduits.isEmpty()) {
            // Clear the flood queue (even though it should be empty)
            floodQueue.clear();

            // Hacky way to get one element from set
            for (IHeatConduit setConduit : m_HeatConduits) {
                floodQueue.add(setConduit);
                break;
            }

            // Create a new network
            IHeatNetwork network = new HeatNetwork();

            // Flood fill through all connected conduits and add them to the
            // network
            while (!floodQueue.isEmpty()) {
                IHeatConduit floodConduit = floodQueue.poll();

                // Remove the conduit from the old network
                m_HeatConduits.remove(floodConduit);

                // Add the conduit to the new network
                network.getHeatConduits().add(floodConduit);
                floodConduit.setNetwork(network);

                // Add adjacent elements to the queue if they've not already
                // been added to the new network
                for (IHeatConduit adjacentConduit : floodConduit.getConnectedConduits()) {
                    if (!network.getHeatConduits().contains(adjacentConduit)) {
                        floodQueue.add(adjacentConduit);
                    }
                }
            }

            // Finally, refresh the network
            network.refresh();
        }

    }
}
