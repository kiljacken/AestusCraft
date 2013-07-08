/**
 * AestusCraft
 * 
 * IHeatProducer.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.api.heat;

public interface IHeatProducer {
    /**
     * Connect the heat producer to a network
     * 
     * @param network
     *            The network to connect the heat producer to
     */
    public void connectToNetwork(IHeatNetwork network);
}
