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
public interface IHeatContainer extends IHeatMachine {
    /**
     * Gets the current heat level of the consumer
     * 
     * @return The consumer's current heat level
     */
    public float getHeatLevel();

    /**
     * Sets the current heat level of the consumer
     * 
     * @param heatLevel
     *            The heat level to set
     */
    public void setHeatLevel(float heatLevel);

    /**
     * Gets the maximum heat level of the consumer
     * 
     * @return The consumer's maximum heat level
     */
    public float getMaxHeatLevel();
}
