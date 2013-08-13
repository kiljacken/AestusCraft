package dk.kiljacken.aestuscraft.api.heat;

/**
 * A tile that consumes heat
 */
public interface IHeatConsumer extends IHeatContainer {
    /**
     * Tries to supply an amount of heat to the consumer
     * 
     * @param amount
     *            The amount to try supplying
     * @return The amount the consumer accepted
     */
    public float supplyHeat(float amount);
}
