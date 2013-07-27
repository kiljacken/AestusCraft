/**
 * AestusCraft
 * 
 * IHeatConductor.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.api.heat;

import java.util.List;

public interface IHeatConductor extends IHeatNetworkConnected {
    /**
     * Gets an array of consumers connected to this conductor
     * 
     * @return An array of consumers connected to this conductor
     */
    public List<IHeatConsumer> getConnectedConsumers();

    /**
     * Gets an array of producers connected to this conductor
     * 
     * @return An array of producers connected to this conductor
     */
    public List<IHeatProducer> getConnectedProducers();

    /**
     * Gets an array of conductors connected to this conductor
     * 
     * @return An array of conductors connected to this conductor
     */
    public List<IHeatConductor> getConnectedConductors();

    /**
     * Gets whether the conductor is valid (e.g it's block has not been removed)
     * 
     * @return Whether the conductor is valid
     */
    public boolean isValid();
}
