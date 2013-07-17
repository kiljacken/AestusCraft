/**
 * AestusCraft
 * 
 * ModBlocks.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import dk.kiljacken.aestuscraft.addon.AddonLoader;
import dk.kiljacken.aestuscraft.lib.BlockIds;
import dk.kiljacken.aestuscraft.lib.StringResources;
import dk.kiljacken.aestuscraft.util.LogHelper;

public class ModBlocks {
    public static Block insulatedFurnace;
    public static Block heatConduit;
    public static Block fuelBurner;

    public static void init() {
        LogHelper.info("Initializing blocks");
        insulatedFurnace = new BlockInsulatedFurnace(BlockIds.INSULATED_FURNACE);
        heatConduit = new BlockHeatConduit(BlockIds.HEAT_CONDUIT);
        fuelBurner = new BlockFuelBurner(BlockIds.FUEL_BURNER);

        GameRegistry.registerBlock(insulatedFurnace, StringResources.INSULATED_FURNACE_NAME);
        GameRegistry.registerBlock(heatConduit, StringResources.HEAT_CONDUIT_NAME);
        GameRegistry.registerBlock(fuelBurner, StringResources.FUEL_BURNER_NAME);

        AddonLoader.instance.initializeAllBlocks();

        initBlockRecipes();
    }

    private static void initBlockRecipes() {
        LogHelper.info("Initializing block recipes");
        GameRegistry.addRecipe(new ItemStack(insulatedFurnace), new Object[] { "www", "wfw", "www", Character.valueOf('w'), Block.cloth, Character.valueOf('f'), Block.furnaceIdle });
        GameRegistry.addRecipe(new ItemStack(heatConduit, 8), new Object[] { "www", "wiw", "www", Character.valueOf('w'), Block.cloth, Character.valueOf('i'), Item.ingotIron });
        GameRegistry.addRecipe(new ItemStack(fuelBurner), new Object[] { "ccc", "cfc", "ccc", 'c', heatConduit, 'f', Block.furnaceIdle });

        AddonLoader.instance.initializeAllBlockRecipes();
    }
}
