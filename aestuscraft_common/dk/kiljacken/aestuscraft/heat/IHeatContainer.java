package dk.kiljacken.aestuscraft.heat;

public interface IHeatContainer {
    /**
     * Query the amount of heat stored in the container.
     * 
     * @return The amount of heat stored
     */
    public int getHeatLevel();
    
    /**
     * Query the amount of heat lost by the container per tick.
     * 
     * @return The amount of heat lost per tick
     */
    public int getHeatLoss();
    
    /**
     * Adds an amount of heat to the container
     * 
     * @param amount The amount to add
     * @return The amount of heat actually added
     */
    public int addHeat(int amount);
    
    /**
     * Remove an amount of heat from the container
     * 
     * @param amount The amount to remove
     * @return The amount of heat actually consumed
     */
    public int removeHeat(int amount);

}