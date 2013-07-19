/**
 * AestusCraft
 * 
 * IHeatNetwork.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.api.heat;

import java.util.Set;

public interface IHeatNetwork {
    /**
     * Refresh the contents of the network
     */
    public void refresh();

    /**
     * Supply an amount of heat to the network
     * 
     * @param amount
     *            The amount to supply
     * @return The amount actually consumed by the network
     */
    public float supplyHeat(float amount);

    /**
     * Query the network for a set of connected heat consumers
     * 
     * @return A set of heat consumers connected to the network
     */
    public Set<IHeatConsumer> getHeatConsumers();

    /**
     * Query the network for a set of connected heat producers
     * 
     * @return A set of heat producers connected to the network
     */
    public Set<IHeatProducer> getHeatProducers();

    /**
     * Query the network for a set contain the conduits that make up the network
     * 
     * @return A set of the heat conduits that make up the network
     */
    public Set<IHeatConduit> getHeatConduits();

    /**
     * Merges the supplied network with this network
     * 
     * @param networks
     *            The network to be merged with this network
     */
    public void merge(IHeatNetwork network);

    /**
     * Split the network into several networks around the given conduit
     * 
     * @param conduit
     *            The conduit to split the network around
     */
    public void split(IHeatConduit conduit);
}
