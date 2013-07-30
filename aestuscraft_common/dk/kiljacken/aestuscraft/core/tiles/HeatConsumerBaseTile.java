/**
 * AestusCraft
 * 
 * HeatConsumerBaseTile.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.tiles;

import dk.kiljacken.aestuscraft.api.heat.IHeatConsumer;

public abstract class HeatConsumerBaseTile extends HeatContainerBaseTile implements IHeatConsumer {
    public HeatConsumerBaseTile(float maxHeatLevel) {
        super(maxHeatLevel);
    }

    @Override
    public float supplyHeat(float amount) {
        amount = Math.min(amount, getMaxHeatLevel() - getHeatLevel());

        setHeatLevel(getHeatLevel() + amount);

        return amount;
    }
}