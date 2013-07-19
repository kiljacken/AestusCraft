/**
 * AestusCraft
 * 
 * TileBoundedHeatConsumer.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.tileentity;

import dk.kiljacken.aestuscraft.lib.StringResources;
import dk.kiljacken.aestuscraft.util.NBTUtil.NBTStorable;
import dk.kiljacken.aestuscraft.util.NBTUtil.NBTValue;

@NBTStorable
public class TileBoundedHeatConsumer extends TileHeatConsumer {
    @NBTValue(name = StringResources.NBT_TE_MAX_HEAT)
    private float m_MaxHeat;

    public TileBoundedHeatConsumer(int maxHeat) {
        super();

        m_MaxHeat = maxHeat;
    }

    @Override
    public float supplyHeat(float amount) {
        amount = Math.min(amount, m_MaxHeat - getHeatLevel());

        return super.supplyHeat(amount);
    }

    @Override
    public boolean isAcceptingHeat() {
        return getHeatLevel() < m_MaxHeat;
    }

    public float getMaxHeat() {
        return m_MaxHeat;
    }
}
