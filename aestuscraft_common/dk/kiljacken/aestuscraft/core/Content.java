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
import cpw.mods.fml.common.registry.GameRegistry;
import dk.kiljacken.aestuscraft.core.blocks.BlockHeatConductor;
import dk.kiljacken.aestuscraft.core.blocks.BlockInfo;
import dk.kiljacken.aestuscraft.core.blocks.MultipleBlockConsumers;
import dk.kiljacken.aestuscraft.core.blocks.MultipleBlockProducers;
import dk.kiljacken.aestuscraft.core.blocks.tiles.TileFuelBurner;
import dk.kiljacken.aestuscraft.core.blocks.tiles.TileHeatConductor;
import dk.kiljacken.aestuscraft.core.blocks.tiles.TileInfo;
import dk.kiljacken.aestuscraft.core.blocks.tiles.TileInsulatedFurnace;
import dk.kiljacken.aestuscraft.core.items.ItemInfo;
import dk.kiljacken.aestuscraft.core.items.ItemNetworkDebugger;
import dk.kiljacken.aestuscraft.core.items.blocks.ItemBlockConsumers;
import dk.kiljacken.aestuscraft.core.items.blocks.ItemBlockProducers;

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

    }
}
