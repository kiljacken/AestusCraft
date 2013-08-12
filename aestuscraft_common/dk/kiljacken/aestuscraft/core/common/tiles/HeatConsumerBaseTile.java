/**
 * AestusCraft
 * 
 * HeatConsumerBaseTile.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.common.tiles;

import dk.kiljacken.aestuscraft.api.heat.IHeatConsumer;

public abstract class HeatConsumerBaseTile extends HeatContainerBaseTile implements IHeatConsumer {
    public HeatConsumerBaseTile(float maxHeatLevel)
    {
        super(maxHeatLevel);
    }

    @Override
    public float supplyHeat(float amount)
    {
        if (amount < 0.0f)
        {
            return 0.0f;
        }

        amount = Math.min(amount, getMaxHeatLevel() - getHeatLevel());

        setHeatLevel(getHeatLevel() + amount);

        return amount;
    }
}
