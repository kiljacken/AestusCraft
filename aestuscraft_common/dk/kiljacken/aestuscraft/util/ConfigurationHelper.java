package dk.kiljacken.aestuscraft.util;

import java.io.File;

import cpw.mods.fml.common.FMLLog;
import dk.kiljacken.aestuscraft.lib.BlockIds;
import dk.kiljacken.aestuscraft.lib.Reference;
import dk.kiljacken.aestuscraft.lib.StringResources;
import net.minecraftforge.common.Configuration;

public class ConfigurationHelper {
    public static Configuration configuration;
    
    public static void init(File file) {
        LogHelper.info("Loading configuration");
        
        configuration = new Configuration(file);
        
        try {
            configuration.load();
        
            BlockIds.INSULATED_FURNACE = configuration.getBlock(StringResources.INSULATED_FURNACE_NAME, BlockIds.INSULATED_FURNACE_DEFAULT).getInt(BlockIds.INSULATED_FURNACE_DEFAULT);
        } catch (Exception e) {
            FMLLog.severe("%s has had a problem loading it's configuration", Reference.MOD_NAME);
        } finally {
            configuration.save();
        }
    }
}
