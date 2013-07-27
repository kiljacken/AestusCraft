/**
 * AestusCraft
 * 
 * Content.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import dk.kiljacken.aestuscraft.AestusCraft;
import dk.kiljacken.aestuscraft.core.blocks.BlockHeatConductor;
import dk.kiljacken.aestuscraft.core.blocks.BlockInfo;
import dk.kiljacken.aestuscraft.core.blocks.MultipleBlockConsumers;
import dk.kiljacken.aestuscraft.core.blocks.MultipleBlockProducers;
import dk.kiljacken.aestuscraft.core.items.ItemInfo;
import dk.kiljacken.aestuscraft.core.items.ItemNetworkDebugger;
import dk.kiljacken.aestuscraft.core.items.blocks.ItemBlockConsumers;
import dk.kiljacken.aestuscraft.core.items.blocks.ItemBlockProducers;
import dk.kiljacken.aestuscraft.core.tiles.TileFrictionHeater;
import dk.kiljacken.aestuscraft.core.tiles.TileFuelBurner;
import dk.kiljacken.aestuscraft.core.tiles.TileHeatConductor;
import dk.kiljacken.aestuscraft.core.tiles.TileInfo;
import dk.kiljacken.aestuscraft.core.tiles.TileInsulatedFurnace;

public class Content {
    public CreativeTabs creativeTab = new CreativeTab("AestusCraft");

    public Block blockProducers;
    public Block blockConsumers;
    public Block blockConductor;

    public Item networkDebugger;

    /**
     * Registers all blocks and tile entities
     */
    public void registerBlocks() {
        blockProducers = Registry.registerBlock(new MultipleBlockProducers(BlockInfo.BLOCK_PRODUCERS_ID), ItemBlockProducers.class, BlockInfo.BLOCK_PRODUCERS_NAME);
        GameRegistry.registerTileEntity(TileFuelBurner.class, TileInfo.FUEL_BURNER_NAME);

        blockConsumers = Registry.registerBlock(new MultipleBlockConsumers(BlockInfo.BLOCK_CONSUMERS_ID), ItemBlockConsumers.class, BlockInfo.BLOCK_CONSUMERS_NAME);
        GameRegistry.registerTileEntity(TileInsulatedFurnace.class, TileInfo.INSULATED_FURNACE_NAME);

        blockConductor = Registry.registerBlock(new BlockHeatConductor(BlockInfo.BLOCK_HEAT_CONDUCTOR_ID), BlockInfo.BLOCK_HEAT_CONDUCTOR_NAME);
        GameRegistry.registerTileEntity(TileHeatConductor.class, TileInfo.HEAT_CONDUCTOR_NAME);

        if (Loader.isModLoaded("BuildCraft|Energy")) {
            GameRegistry.registerTileEntity(TileFrictionHeater.class, TileInfo.FRICTION_HEATER_NAME);
        }
    }

    /**
     * Registers all items
     */
    public void registerItems() {
        networkDebugger = Registry.registerItem(new ItemNetworkDebugger(ItemInfo.NETWORK_DEBUGGER_ID), ItemInfo.NETWORK_DEBUGGER_NAME);
    }

    /**
     * Registers all recipes
     */
    public void registerRecipes() {
        GameRegistry.addShapedRecipe(new ItemStack(blockConsumers, 1, MultipleBlockConsumers.META_INSULATED_FURNACE), "ccc", "cfc", "ccc", 'c', Block.cloth, 'f', Block.furnaceIdle);
        GameRegistry.addShapedRecipe(new ItemStack(blockProducers, 1, MultipleBlockProducers.META_FUEL_BURNER), "ccc", "cfc", "ccc", 'c', blockConductor, 'f', Block.furnaceIdle);
        GameRegistry.addShapedRecipe(new ItemStack(blockConductor, 8), "ccc", "cic", "ccc", 'c', Block.cloth, 'i', Item.ingotIron);

        if (Loader.isModLoaded("BuildCraft|Energy")) {
            boolean buildcraftItems = false;
            Item woodenGear = null;
            Item stoneGear = null;
            
            try {
                woodenGear = (Item) Class.forName("buildcraft.BuildCraftCore").getField("woodenGearItem").get(null);
                stoneGear = (Item) Class.forName("buildcraft.BuildCraftCore").getField("stoneGearItem").get(null);
                buildcraftItems = true;
            } catch (Exception ex) {
                AestusCraft.log.severe("Unable to find Buildcraft items");
            }
            
            if (buildcraftItems) {
                GameRegistry.addShapedRecipe(new ItemStack(blockProducers, 1, MultipleBlockProducers.META_FRICTION_HEATER), "scs", "i i", "WwW", 's', Block.stone, 'c', blockConductor, 'i', stoneGear, 'W', Block.cloth, 'w', woodenGear);
            }
        }
    }
}
