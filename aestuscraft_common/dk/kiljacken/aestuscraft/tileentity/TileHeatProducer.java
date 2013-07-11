/**
 * AestusCraft
 * 
 * TileTestHeatProducer.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.tileentity;

import dk.kiljacken.aestuscraft.api.heat.IHeatNetwork;
import dk.kiljacken.aestuscraft.api.heat.IHeatProducer;

public class TileHeatProducer extends TileAEC implements IHeatProducer {
    protected IHeatNetwork m_HeatNetwork;

    @Override
    public void connectToNetwork(IHeatNetwork network) {
        m_HeatNetwork = network;
    }
}
