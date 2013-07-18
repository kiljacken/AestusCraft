/**
 * AestusCraft
 * 
 * TileHeatContainer.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.tileentity;

import dk.kiljacken.aestuscraft.api.heat.IHeatConsumer;
import dk.kiljacken.aestuscraft.lib.StringResources;
import dk.kiljacken.aestuscraft.util.NBTUtil.NBTStorable;
import dk.kiljacken.aestuscraft.util.NBTUtil.NBTValue;

@NBTStorable
public abstract class TileHeatConsumer extends TileAEC implements IHeatConsumer {
    @NBTValue(name = StringResources.NBT_TE_HEAT_LEVEL)
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
}
