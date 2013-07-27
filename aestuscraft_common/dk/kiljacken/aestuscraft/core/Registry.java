/**
 * AestusCraft
 * 
 * Registry.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import cpw.mods.fml.common.registry.GameRegistry;
import dk.kiljacken.aestuscraft.AestusCraft;

public class Registry {
    private static Map<String, Block> m_BlockMap;
    private static Map<String, Item> m_ItemMap;

    /**
     * Initializes the registry
     */
    public static void initialize() {
        m_BlockMap = new HashMap<>();
        m_ItemMap = new HashMap<>();
    }

    /**
     * Registers a block
     * 
     * @param blockClass Class of the block
     * @param itemBlockClass Class of the blocks item form
     * @param name Name of the block
     * @param id Id of the block
     * @return The instantiated and registered block
     */
    public static Block registerBlock(Block block, Class<? extends ItemBlock> itemBlockClass, String name) {
        block.setCreativeTab(AestusCraft.content.creativeTab);
        block.setUnlocalizedName(name);

        GameRegistry.registerBlock(block, itemBlockClass, name);
        m_BlockMap.put(name, block);

        return block;
    }

    /**
     * Registers a block with default block item
     * 
     * @param blockClass Class of the block
     * @param name Name of the block
     * @param id Id of the block
     * @return The instantiated and registered block
     */
    public static Block registerBlock(Block block, String name) {
        return registerBlock(block, ItemBlock.class, name);
    }

    /**
     * Registers an item
     * 
     * @param itemClass Class of the item
     * @param name Name of the item
     * @param id Id of the item
     * @return The instantiated and registered item
     */
    public static Item registerItem(Item item, String name) {
        item.setCreativeTab(AestusCraft.content.creativeTab);
        item.setUnlocalizedName(name);

        GameRegistry.registerItem(item, name);
        m_ItemMap.put(name, item);

        return item;
    }
}
