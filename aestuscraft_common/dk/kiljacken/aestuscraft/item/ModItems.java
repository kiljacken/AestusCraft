/**
 * AestusCraft
 * 
 * ModItems.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.item;

import net.minecraft.item.Item;
import dk.kiljacken.aestuscraft.lib.ItemIds;
import dk.kiljacken.aestuscraft.util.LogHelper;

public class ModItems {
    public static Item conduitDebugger;

    public static void init() {
        LogHelper.info("Initializing items");
        conduitDebugger = new ItemConduitDebugger(ItemIds.CONDUIT_DEBUGGER);
    }

    public static void initRecipes() {
        LogHelper.info("Initializing item recipes");
    }

}
