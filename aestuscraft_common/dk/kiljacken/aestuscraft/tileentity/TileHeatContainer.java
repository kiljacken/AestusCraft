package dk.kiljacken.aestuscraft.tileentity;

import dk.kiljacken.aestuscraft.heat.IHeatContainer;
import dk.kiljacken.aestuscraft.lib.StringResources;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileHeatContainer extends TileAEC implements IHeatContainer {
    private int m_HeatLevel;

    @Override
    public abstract int getHeatLoss();
    
    public abstract int getHeatConductivity();
    
    @Override
    public int getHeatLevel() {
        return m_HeatLevel;
    }
    
    @Override
    public int addHeat(int amount) {
        int actualAmount = Math.min(amount, getHeatConductivity());
        
        m_HeatLevel += actualAmount;
        
        return actualAmount;
    }

    @Override
    public int removeHeat(int amount) {
        int actualAmount = Math.min(amount, getHeatConductivity());
        actualAmount = Math.min(actualAmount, m_HeatLevel);
        
        // Make sure we don't go into negative heat
        m_HeatLevel -= actualAmount;
        
        return actualAmount;
    }
    
    @Override
    public void updateEntity() {
        // Apply heat loss every tick but make sure we don't go into the negatives 
        m_HeatLevel -= Math.min(getHeatLoss(), m_HeatLevel);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        
        m_HeatLevel = nbtTagCompound.getInteger(StringResources.NBT_TE_HEAT_LEVEL);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        
        nbtTagCompound.setInteger(StringResources.NBT_TE_HEAT_LEVEL, m_HeatLevel);
    }
}
