/**
 * AestusCraft
 * 
 * TileBoundedHeatConsumer.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import dk.kiljacken.aestuscraft.lib.StringResources;

public class TileBoundedHeatConsumer extends TileHeatConsumer {
    private int m_MaxHeat;

    public TileBoundedHeatConsumer(int maxHeat) {
        super();

        m_MaxHeat = maxHeat;
    }

    @Override
    public int supplyHeat(int amount) {
        amount = Math.min(amount, m_MaxHeat - getHeatLevel());

        return super.supplyHeat(amount);
    }

    @Override
    public boolean isAcceptingHeat() {
        return getHeatLevel() < m_MaxHeat;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        m_MaxHeat = nbtTagCompound.getInteger(StringResources.NBT_TE_MAX_HEAT);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        nbtTagCompound.setInteger(StringResources.NBT_TE_MAX_HEAT, m_MaxHeat);

    }

}
