/**
 * AestusCraft
 * 
 * IHeatConsumer.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.api.heat;

public interface IHeatConsumer {
    /**
     * Queries whether the heat consumer is accepting more heat
     * 
     * @return {@code true} if the heat consumer is accepting heat,
     *         {@code false} if not
     */
    public boolean isAcceptingHeat();

    /**
     * Supply the heat consumer an amount of heat
     * 
     * @param amount
     *            The amount to supply
     * @return The actual amount consumed by the heat consumer
     */
    public float supplyHeat(float amount);

}
