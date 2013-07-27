/**
 * AestusCraft
 * 
 * AestusCraftAPI.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.api;

import java.util.Map;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.ReflectionHelper;
import dk.kiljacken.aestuscraft.library.ReflectionUtil;

public class AestusCraftAPI {
    public static final String MOD_ID = "aestuscraft";
    
    public static Logger log;
    private static boolean m_Initialized = false;
    private static Map<String, Block> m_BlockMap;
    private static Map<String, Item> m_ItemMap;

    /**
     * Initializes the AestusCraft API.
     * 
     * Only AestusCraft should call this method.
     */
    public static void initialize() {
        if (m_Initialized) {
            return;
        }

        AestusCraftAPI.log = Logger.getLogger("AestusCraft-API");
        AestusCraftAPI.log.setParent(FMLLog.getLogger());

        Class<?> registryClass = ReflectionHelper.getClass(AestusCraftAPI.class.getClassLoader(), "dk.kiljacken.aestuscraft.core.Registry");
        try {
            Object nil = null;

            m_BlockMap = ReflectionUtil.getPrivateValue(registryClass, nil, "m_BlockMap");
            m_ItemMap = ReflectionUtil.getPrivateValue(registryClass, nil, "m_ItemMap");

            m_Initialized = true;
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            AestusCraftAPI.log.severe("Exception while initializing AestusCraft API");
            AestusCraftAPI.log.severe("Parent mod: " + Loader.instance().activeModContainer().getName());
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a block registered by AestusCraft
     * 
     * @param name Name of the block
     * @return The block
     */
    public static Block getBlock(String name) {
        return m_BlockMap.get(name);
    }

    /**
     * Get an item registered by AestusCraft
     * 
     * @param name Name of the item
     * @return The item
     */
    public static Item getItem(String name) {
        return m_ItemMap.get(name);
    }
}
