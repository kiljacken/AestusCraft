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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import dk.kiljacken.aestuscraft.AestusCraft;
import dk.kiljacken.aestuscraft.core.blocks.BlockFrictionHeater;
import dk.kiljacken.aestuscraft.core.blocks.BlockFuelBurner;
import dk.kiljacken.aestuscraft.core.blocks.BlockHeatConductor;
import dk.kiljacken.aestuscraft.core.blocks.BlockHeatedFlooring;
import dk.kiljacken.aestuscraft.core.blocks.BlockInfo;
import dk.kiljacken.aestuscraft.core.blocks.BlockInsulatedFurnace;
import dk.kiljacken.aestuscraft.core.items.ItemInfo;
import dk.kiljacken.aestuscraft.core.items.ItemNetworkDebugger;
import dk.kiljacken.aestuscraft.core.tiles.TileFrictionHeater;
import dk.kiljacken.aestuscraft.core.tiles.TileFuelBurner;
import dk.kiljacken.aestuscraft.core.tiles.TileHeatConductor;
import dk.kiljacken.aestuscraft.core.tiles.TileHeatedFlooring;
import dk.kiljacken.aestuscraft.core.tiles.TileInfo;
import dk.kiljacken.aestuscraft.core.tiles.TileInsulatedFurnace;

public class Content {
    public CreativeTab creativeTab = new CreativeTab("AestusCraft");

    public Block blockConductor;
    public Block blockFuelBurner;
    public Block blockInsulatedFurnace;
    public Block blockHeatedFlooring;
    public Block blockFrictionHeater;

    public Item networkDebugger;

    /**
     * Registers all blocks and tile entities
     */
    public void registerBlocks() {
        blockConductor = Registry.registerBlock(new BlockHeatConductor(BlockInfo.BLOCK_HEAT_CONDUCTOR_ID), BlockInfo.BLOCK_HEAT_CONDUCTOR_NAME);
        GameRegistry.registerTileEntity(TileHeatConductor.class, TileInfo.HEAT_CONDUCTOR_NAME);

        blockFuelBurner = Registry.registerBlock(new BlockFuelBurner(BlockInfo.BLOCK_FUEL_BURNER_ID), BlockInfo.BLOCK_FUEL_BURNER_NAME);
        GameRegistry.registerTileEntity(TileFuelBurner.class, TileInfo.FUEL_BURNER_NAME);

        blockInsulatedFurnace = Registry.registerBlock(new BlockInsulatedFurnace(BlockInfo.BLOCK_INSULATED_FURNACE_ID), BlockInfo.BLOCK_INSULATED_FURNACE_NAME);
        GameRegistry.registerTileEntity(TileInsulatedFurnace.class, TileInfo.INSULATED_FURNACE_NAME);

        blockHeatedFlooring = Registry.registerBlock(new BlockHeatedFlooring(BlockInfo.BLOCK_HEATED_FLOORING_ID), BlockInfo.BLOCK_HEATED_FLOORING_NAME);
        GameRegistry.registerTileEntity(TileHeatedFlooring.class, TileInfo.HEATED_FLOORING_NAME);

        if (Loader.isModLoaded("BuildCraft|Energy")) {
            blockFrictionHeater = Registry.registerBlock(new BlockFrictionHeater(BlockInfo.BLOCK_FRICTION_HEATER_ID), BlockInfo.BLOCK_FRICTION_HEATER_NAME);
            GameRegistry.registerTileEntity(TileFrictionHeater.class, TileInfo.FRICTION_HEATER_NAME);
        }

        creativeTab.setIconItemStack(new ItemStack(blockInsulatedFurnace));
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
        GameRegistry.addShapedRecipe(new ItemStack(blockInsulatedFurnace), "ccc", "cfc", "ccc", 'c', Block.cloth, 'f', Block.furnaceIdle);
        GameRegistry.addShapedRecipe(new ItemStack(blockFuelBurner), "ccc", "cfc", "ccc", 'c', blockConductor, 'f', Block.furnaceIdle);
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
                GameRegistry.addShapedRecipe(new ItemStack(blockFrictionHeater), "scs", "i i", "WwW", 's', Block.stone, 'c', blockConductor, 'i', stoneGear, 'W', Block.cloth, 'w', woodenGear);
            }
        }
    }
}
