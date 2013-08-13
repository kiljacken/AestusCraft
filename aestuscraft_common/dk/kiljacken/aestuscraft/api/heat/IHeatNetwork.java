package dk.kiljacken.aestuscraft.api.heat;

import java.util.List;

public interface IHeatNetwork {
    /**
     * Gets a list of machines connected to this network
     * 
     * @return A list of machines connected to this network
     */
    public List<IHeatMachine> getConnectedMachines();

    /**
     * Gets a list of conductors that are part of this network
     * 
     * @return A list of conductors connected to this network
     */
    public List<IHeatConductor> getConnectedConductors();

    /**
     * Merges two networks
     * 
     * @param network
     *            The network to merge with this one
     */
    public void merge(IHeatNetwork network);

    /**
     * Splits a network into the several network by removing a conductor
     * 
     * @param conductor
     *            The conductor to remove
     */
    public void split(IHeatConductor conductor);

    /**
     * Refreshes the lists of connected consumers and producers
     */
    public void refresh();

    /**
     * Tries to supply an amount of energy to the network. The energy will be
     * evenly spread among the connected consumers that have space for more heat
     * 
     * @param amount
     *            Amount of heat to try suppling
     * @return The amount accepted by the network
     */
    public float supplyHeat(float amount);
}
