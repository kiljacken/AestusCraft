/**
 * AestusCraft
 * 
 * StringResources.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.lib;

import java.io.File;

public class StringResources {
    // Paths
    public static final String PATH_CONFIGURATION = Reference.MOD_ID + File.separator + Reference.MOD_ID + ".cfg";

    // Block names
    public static final String INSULATED_FURNACE_NAME = "insulatedFurnace";
    public static final String HEAT_CONDUIT_NAME = "heatConduit";
    public static final String FUEL_BURNER_NAME = "fuelBurner";

    // Tile entity names
    public static final String TE_INSULATED_FURNACE_NAME = "tileInsulatedFurnace";
    public static final String TE_HEAT_CONDUIT_NAME = "tileHeatConduit";
    public static final String TE_FUEL_BURNER_NAME = "tileFuelBurner";

    // Container names
    public static final String CONTAINER_INSULATED_FURNACE_NAME = "container.insulatedFurnace";
    public static final String CONTAINER_FUEL_BURNER_NAME = "container.fuelBurner";

    // Item names
    public static final String ITEM_CONDUIT_DEBUGGER_NAME = "conduitDebugger";

    // NBT tag names
    public static final String NBT_TE_CUSTOM_NAME = "CustomName";
    public static final String NBT_TE_ORIENTATION = "teOrientation";
    public static final String NBT_TE_INVENTORY_SLOT = "slot";
    public static final String NBT_TE_INVENTORY_ITEMS = "items";
    public static final String NBT_TE_HEAT_LEVEL = "heatLevel";
    public static final String NBT_TE_MAX_HEAT = "maxHeat";
    public static final String NBT_TE_FUEL_LEFT = "fuelLeft";
    public static final String NBT_TE_FUEL_HEAT = "fuelHeat";
    public static final String NBT_TE_HEATING_LEFT = "heatingLeft";
}
