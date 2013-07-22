/**
 * AestusCraft
 * 
 * IHeatNetwork.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.api.heat;

import java.util.List;

public interface IHeatNetwork {
    /**
     * Gets an array of consumers connected to this network
     * 
     * @return An array of consumers connected to this conductor
     */
    public List<IHeatConsumer> getConnectedConsumers();

    /**
     * Gets an array of producers connected to this network
     * 
     * @return An array of producers connected to this conductor
     */
    public List<IHeatProducer> getConnectedProducers();

    /**
     * Gets an array of conductors connected to this network
     * 
     * @return An array of conductors connected to this conductor
     */
    public List<IHeatConductor> getConnectedConductors();

    /**
     * Adds a heat conductor and all connected consumers, producers and
     * conductors to the network
     * 
     * @param conductor The conductor to add
     */
    public void addConductor(IHeatConductor conductor);

    /**
     * Removes a conductor and all connected consumers, producers and conductors
     * that are no longer available from the network
     * 
     * @param conductor The conductor to remove
     */
    public void removeConductor(IHeatConductor conductor);

    /**
     * Tries to supply an amount of energy to the network. The energy will be
     * evenly spread among the connected consumers that have space for more heat
     * 
     * @param amount Amount of heat to try suppling
     * @return The amount accepted by the network
     */
    public float supplyHeat(float amount);
}
