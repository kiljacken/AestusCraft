/**
 * AestusCraft
 * 
 * TileHeatContainer.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import dk.kiljacken.aestuscraft.api.heat.IHeatConsumer;
import dk.kiljacken.aestuscraft.lib.StringResources;

public abstract class TileHeatConsumer extends TileAEC implements IHeatConsumer {
    private int m_HeatLevel;

    @Override
    public boolean isAcceptingHeat() {
        return true;
    }

    @Override
    public int supplyHeat(int amount) {
        m_HeatLevel += amount;

        return amount;
    }

    public int getHeatLevel() {
        return m_HeatLevel;
    }

    protected int removeHeat(int amount) {
        amount = Math.min(amount, m_HeatLevel);

        m_HeatLevel -= amount;

        return amount;
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
