/**
 * AestusCraft
 * 
 * HeatNetwork.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.api.heat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class HeatNetworkImpl implements IHeatNetwork {
    private List<IHeatConsumer> m_ConnectedConsumers;
    private List<IHeatProducer> m_ConnectedProducers;
    private List<IHeatConductor> m_ConnectedConductors;

    public HeatNetworkImpl() {
        m_ConnectedConsumers = new ArrayList<>();
        m_ConnectedProducers = new ArrayList<>();
        m_ConnectedConductors = new ArrayList<>();
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
    public void merge(IHeatNetwork network) {
        if (network == null || network == this) {
            return;
        }

        IHeatNetwork newNetwork = new HeatNetworkImpl();
        newNetwork.getConnectedConductors().addAll(getConnectedConductors());
        newNetwork.getConnectedConductors().addAll(network.getConnectedConductors());
        newNetwork.refresh();
    }

    @Override
    public void split(IHeatConductor conductor) {
        // Remove the conductor from this network
        m_ConnectedConductors.remove(conductor);

        // Clear lists to make avoid dead references
        m_ConnectedConsumers.clear();
        m_ConnectedProducers.clear();

        // Initialize flood fill queue
        Queue<IHeatConductor> floodQueue = new LinkedList<>();

        // Loop until the conductors in this network has been removed
        while (!m_ConnectedConductors.isEmpty()) {
            // Get a conductor from this network and remove it. We get the last one as it's best for performance
            floodQueue.add(m_ConnectedConductors.remove(m_ConnectedConductors.size() - 1));

            // Create a new network
            IHeatNetwork network = new HeatNetworkImpl();

            // Loop through all conductors connected to this one and add them to the network
            while (!floodQueue.isEmpty()) {
                IHeatConductor floodConductor = floodQueue.poll();

                // Add the conductor to the new network
                network.getConnectedConductors().add(floodConductor);
                floodConductor.setNetwork(network);

                // Add adjacent conductors to the queue if they're not already in the new network
                for (IHeatConductor connectedConductor : floodConductor.getConnectedConductors()) {
                    if (connectedConductor != conductor && !network.getConnectedConductors().contains(connectedConductor)) {
                        floodQueue.add(connectedConductor);
                    }
                }
            }

            network.refresh();
        }
    }

    @Override
    public void refresh() {
        int size = getConnectedConductors().size() * 5;
        Set<IHeatConsumer> consumers = new HashSet<>(size);
        Set<IHeatProducer> producers = new HashSet<>(size);

        Iterator<IHeatConductor> iter = getConnectedConductors().iterator();

        while (iter.hasNext()) {
            IHeatConductor conductor = iter.next();

            if (conductor == null || !conductor.isValid()) {
                iter.remove();
            } else {
                conductor.setNetwork(this);

                consumers.addAll(conductor.getConnectedConsumers());
                producers.addAll(conductor.getConnectedProducers());
            }
        }

        getConnectedConsumers().clear();
        getConnectedConsumers().addAll(consumers);

        getConnectedProducers().clear();
        getConnectedProducers().addAll(producers);

        for (IHeatProducer producer : getConnectedProducers()) {
            producer.setNetwork(this);
        }
    }

    @Override
    public float supplyHeat(float amount) {
        int consumersWithSpace = 0;
        for (IHeatConsumer consumer : getConnectedConsumers()) {
            if (consumer.getHeatLevel() < consumer.getMaxHeatLevel()) {
                consumersWithSpace++;
            }
        }

        float consumedAmount = 0;
        for (IHeatConsumer consumer : getConnectedConsumers()) {
            if (consumer.getHeatLevel() < consumer.getMaxHeatLevel()) {
                consumedAmount += consumer.supplyHeat(amount - consumedAmount / consumersWithSpace);

                consumersWithSpace--;
            }
        }

        return consumedAmount;
    }
}
