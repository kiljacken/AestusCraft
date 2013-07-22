/**
 * AestusCraft
 * 
 * HeatProducerBaseTile.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.blocks.tiles;

import dk.kiljacken.aestuscraft.api.heat.IHeatNetwork;
import dk.kiljacken.aestuscraft.api.heat.IHeatProducer;

public abstract class HeatProducerBaseTile extends HeatContainerBaseTile implements IHeatProducer {
    private IHeatNetwork m_HeatNetwork;

    public HeatProducerBaseTile(float maxHeatLevel) {
        super(maxHeatLevel);
    }

    @Override
    public IHeatNetwork getNetwork() {
        return m_HeatNetwork;
    }

    @Override
    public void setNetwork(IHeatNetwork network) {
        m_HeatNetwork = network;
    }
}
