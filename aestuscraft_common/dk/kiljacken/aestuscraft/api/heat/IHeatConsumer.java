/**
 * AestusCraft
 * 
 * IHeatConsumer.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.api.heat;

/**
 * A tile that consumes heat
 */
public interface IHeatConsumer extends IHeatContainer {
    /**
     * Tries to supply an amount of heat to the consumer
     * 
     * @param amount The amount to try supplying
     * @return The amount the consumer accepted
     */
    public float supplyHeat(float amount);
}
