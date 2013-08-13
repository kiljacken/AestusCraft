package dk.kiljacken.aestuscraft.api.heat;

/**
 * A tile that can connect to a heat network
 */
public interface IHeatMachine {
    /**
     * Gets the heat network the producer is connected to
     * 
     * @return The heat network the producer is connected to
     */
    public IHeatNetwork getNetwork();

    /**
     * Sets the heat network the producer is connected to
     * 
     * @param network
     *            The heat network the producer is connected to
     */
    public void setNetwork(IHeatNetwork network);
}
