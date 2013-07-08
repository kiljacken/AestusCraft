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

    // Tile entity names
    public static final String TE_INSULATED_FURNACE_NAME = "tileInsulatedFurnace";

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
