/**
 * AestusCraft
 * 
 * HeatContainerBaseTile.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.blocks.tiles;

import dk.kiljacken.aestuscraft.api.heat.IHeatContainer;
import dk.kiljacken.aestuscraft.library.nbt.NBTUtil.NBTValue;

public abstract class HeatContainerBaseTile extends BaseTile implements IHeatContainer {
    @NBTValue(name = "heatLevel")
    protected float m_HeatLevel;
    private float m_MaxHeatLevel;

    public HeatContainerBaseTile(float maxHeatLevel) {
        m_MaxHeatLevel = maxHeatLevel;
    }

    @Override
    public float getHeatLevel() {
        return m_HeatLevel;
    }

    @Override
    public float getMaxHeatLevel() {
        return m_MaxHeatLevel;
    }
}
