/**
 * AestusCraft
 * 
 * ConfigurationHelper.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.util;

import java.io.File;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;
import dk.kiljacken.aestuscraft.addon.AddonLoader;
import dk.kiljacken.aestuscraft.lib.BlockIds;
import dk.kiljacken.aestuscraft.lib.ItemIds;
import dk.kiljacken.aestuscraft.lib.Reference;
import dk.kiljacken.aestuscraft.lib.StringResources;

public class ConfigurationHelper {
    public static Configuration configuration;

    public static void init(File file) {
        LogHelper.info("Loading configuration");

        configuration = new Configuration(file);

        try {
            configuration.load();

            // Load block ids
            BlockIds.INSULATED_FURNACE = configuration.getBlock(StringResources.INSULATED_FURNACE_NAME, BlockIds.INSULATED_FURNACE_DEFAULT).getInt(BlockIds.INSULATED_FURNACE_DEFAULT);
            BlockIds.HEAT_CONDUIT = configuration.getBlock(StringResources.HEAT_CONDUIT_NAME, BlockIds.HEAT_CONDUIT_DEFAULT).getInt(BlockIds.HEAT_CONDUIT_DEFAULT);
            BlockIds.FUEL_BURNER = configuration.getBlock(StringResources.FUEL_BURNER_NAME, BlockIds.FUEL_BURNER_DEFAULT).getInt(BlockIds.FUEL_BURNER_DEFAULT);

            // Load item ids
            ItemIds.CONDUIT_DEBUGGER = configuration.getItem(StringResources.ITEM_CONDUIT_DEBUGGER_NAME, ItemIds.CONDUIT_DEBUGGER_DEFAULT).getInt(ItemIds.CONDUIT_DEBUGGER_DEFAULT) - 256;

            AddonLoader.instance.loadAllConfigs(configuration);
        } catch (Exception e) {
            FMLLog.severe("%s has had a problem loading it's configuration", Reference.MOD_NAME);
        } finally {
            configuration.save();
        }
    }
}
