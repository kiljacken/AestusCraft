/**
 * AestusCraft
 * 
 * IHeatConductor.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.api.heat;

public interface IHeatConductor extends IHeatNetworkConnected {
    /**
     * Gets an array of consumers connected to this conductor
     * 
     * @return An array of consumers connected to this conductor
     */
    public IHeatConsumer[] getConnectedConsumers();

    /**
     * Gets an array of producers connected to this conductor
     * 
     * @return An array of producers connected to this conductor
     */
    public IHeatProducer[] getConnectedProducers();

    /**
     * Gets an array of conductors connected to this conductor
     * 
     * @return An array of conductors connected to this conductor
     */
    public IHeatConductor[] getConnectedConductors();
}
