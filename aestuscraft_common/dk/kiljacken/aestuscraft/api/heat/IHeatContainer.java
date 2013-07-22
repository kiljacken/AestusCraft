/**
 * AestusCraft
 * 
 * IHeatContainer.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.api.heat;

/**
 * A tile that contains heat
 */
public interface IHeatContainer {
    /**
     * Gets the current heat level of the consumer
     * 
     * @return The consumer's current heat level
     */
    public float getHeatLevel();

    /**
     * Gets the maximum heat level of the consumer
     * 
     * @return The consumer's maximum heat level
     */
    public float getMaxHeatLevel();
}
