/**
 * AestusCraft
 * 
 * BlockInfo.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.blocks;

public class BlockInfo {
    public static int BLOCK_PRODUCERS_ID = 2501;
    public static final String BLOCK_PRODUCERS_NAME = "producers";
    public static final String[] BLOCK_PRODUCERS_NAME_META = { "fuelBurner", "frictionHeater" };

    public static int BLOCK_CONSUMERS_ID = 2502;
    public static final String BLOCK_CONSUMERS_NAME = "consumers";
    public static final String[] BLOCK_CONSUMERS_NAME_META = { "insulatedFurnace" };

    public static int BLOCK_HEAT_CONDUCTOR_ID = 2503;
    public static final String BLOCK_HEAT_CONDUCTOR_NAME = "heatConductor";
}
