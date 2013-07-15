/**
 * AestusCraft
 * 
 * IHeatConduit.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.api.heat;

import java.util.Set;

public interface IHeatConduit {
    /**
     * Get the network this conduit is part of
     * 
     * @return The network this conduit is part of
     */
    public IHeatNetwork getNetwork();

    /**
     * Set the network this conduit is part of
     * 
     * @param network
     *            The network this conduit is part of
     */
    public void setNetwork(IHeatNetwork network);

    /**
     * Query the conduit for a set of conduits connected to it
     * 
     * @return A set containing the conduits connected to this conduit
     */
    public Set<IHeatConduit> getConnectedConduits();

    /**
     * Query the conduit for a set of consumers connected to it
     * 
     * @return A set containing the consumers connected to this conduit
     */
    public Set<IHeatConsumer> getConnectedConsumers();

    /**
     * Query the conduit for a set of producers connected to it
     * 
     * @return A set containing the conduits connected to this conduit
     */
    public Set<IHeatProducer> getConnectedProducers();

    /**
     * Query whether the conduit is invalid and should be removed from the
     * network
     * 
     * @return {@code true} if the conduit is valid, {@code false} if not
     */
    public boolean isValid();
}
