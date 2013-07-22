/**
 * AestusCraft
 * 
 * HeatNetwork.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.heat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dk.kiljacken.aestuscraft.api.heat.IHeatConductor;
import dk.kiljacken.aestuscraft.api.heat.IHeatConsumer;
import dk.kiljacken.aestuscraft.api.heat.IHeatNetwork;
import dk.kiljacken.aestuscraft.api.heat.IHeatProducer;

public class HeatNetwork implements IHeatNetwork {
    private List<IHeatConsumer> m_ConnectedConsumers;
    private List<IHeatProducer> m_ConnectedProducers;
    private List<IHeatConductor> m_ConnectedConductors;

    public HeatNetwork() {
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
    public void addConductor(IHeatConductor conductor) {
        IHeatNetwork network = conductor.getNetwork();

        if (network != null && network != this) {
            for (IHeatConsumer connectedConsumer : network.getConnectedConsumers()) {
                if (!m_ConnectedConsumers.contains(connectedConsumer)) {
                    m_ConnectedConsumers.add(connectedConsumer);
                }
            }

            for (IHeatProducer connectedProducer : network.getConnectedProducers()) {
                if (!m_ConnectedProducers.contains(connectedProducer)) {
                    m_ConnectedProducers.add(connectedProducer);
                    connectedProducer.setNetwork(this);
                }
            }

            for (IHeatConductor connectedConductor : network.getConnectedConductors()) {
                if (!m_ConnectedConductors.contains(connectedConductor)) {
                    m_ConnectedConductors.add(connectedConductor);
                    connectedConductor.setNetwork(this);
                }
            }
        }
    }

    @Override
    public void removeConductor(IHeatConductor conductor) {
        m_ConnectedConductors.remove(conductor);

        Queue<IHeatConductor> floodQueue = new LinkedList<>();
        while (!m_ConnectedConductors.isEmpty()) {
            floodQueue.clear();
            floodQueue.add(m_ConnectedConductors.remove(m_ConnectedConductors.size() - 1));

            IHeatNetwork network = new HeatNetwork();
            while (!floodQueue.isEmpty()) {
                IHeatConductor floodConductor = floodQueue.poll();

                network.getConnectedConductors().add(floodConductor);
                floodConductor.setNetwork(network);

                for (IHeatConsumer connectedConsumer : floodConductor.getConnectedConsumers()) {
                    if (!network.getConnectedConsumers().contains(connectedConsumer)) {
                        network.getConnectedConsumers().add(connectedConsumer);
                    }
                }

                for (IHeatProducer connectedProducer : floodConductor.getConnectedProducers()) {
                    if (!network.getConnectedProducers().contains(connectedProducer)) {
                        network.getConnectedProducers().add(connectedProducer);
                        connectedProducer.setNetwork(this);
                    }
                }

                for (IHeatConductor connectedConductor : floodConductor.getConnectedConductors()) {
                    if (connectedConductor != conductor && conductor.getNetwork() == this) {
                        floodQueue.add(connectedConductor);
                    }
                }
            }
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
