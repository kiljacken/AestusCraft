/**
 * AestusCraft
 * 
 * IHeatNetworkConnected.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.api.heat;

/**
 * A tile that can connect to a heat network
 */
public interface IHeatNetworkConnected {
    /**
     * Gets the heat network the producer is connected to
     * 
     * @return The heat network the producer is connected to
     */
    public IHeatNetwork getNetwork();

    /**
     * Sets the heat network the producer is connected to
     * 
     * @param network The heat network the producer is connected to
     */
    public void setNetwork(IHeatNetwork network);
}
