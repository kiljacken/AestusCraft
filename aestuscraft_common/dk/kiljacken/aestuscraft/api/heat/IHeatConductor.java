/**
 * AestusCraft
 * 
 * IHeatConductor.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.api.heat;

import java.util.List;

public interface IHeatConductor extends IHeatMachine {
    /**
     * Gets a list of machines connected to this conductor
     * 
     * @return A list of machines connected to this conductor
     */
    public List<IHeatMachine> getConnectedMachines();

    /**
     * Gets a list of conductors connected to this conductor
     * 
     * @return A list of conductors connected to this conductor
     */
    public List<IHeatConductor> getConnectedConductors();

    /**
     * Query if the conductor supplies additional machine functionality (e.g.
     * it's a consumer)
     * 
     * @return Whether the conductor has additional functionality
     */
    public boolean hasMachineFunctionality();
}
