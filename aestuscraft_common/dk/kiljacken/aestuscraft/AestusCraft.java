/**
 * AestusCraft
 * 
 * AestusCraft.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import dk.kiljacken.aestuscraft.addon.AddonLoader;
import dk.kiljacken.aestuscraft.block.ModBlocks;
import dk.kiljacken.aestuscraft.core.proxy.CommonProxy;
import dk.kiljacken.aestuscraft.item.ModItems;
import dk.kiljacken.aestuscraft.lib.Reference;
import dk.kiljacken.aestuscraft.lib.StringResources;
import dk.kiljacken.aestuscraft.network.PacketHandler;
import dk.kiljacken.aestuscraft.util.ConfigurationHelper;
import dk.kiljacken.aestuscraft.util.LocalizationHelper;
import dk.kiljacken.aestuscraft.util.LogHelper;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
@NetworkMod(clientSideRequired = true, serverSideRequired = true, channels = { Reference.CHANNEL }, packetHandler = PacketHandler.class)
public class AestusCraft {
    @Instance(Reference.MOD_ID)
    public static AestusCraft instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    public static CreativeTabs creativeTab = new CreativeTabs(StringResources.CREATIVE_TAB_NAME) {
        @Override
        public ItemStack getIconItemStack() {
            return new ItemStack(ModBlocks.insulatedFurnace);
        };
    };

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LogHelper.init();

        AddonLoader.instance.initAll();

        LocalizationHelper.init();

        ConfigurationHelper.init(new File(event.getModConfigurationDirectory(), StringResources.PATH_CONFIGURATION));

        ModBlocks.init();
        ModItems.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.instance().registerGuiHandler(instance, proxy);

        proxy.registerTileEntities();
        proxy.initializeRendering();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        AddonLoader.instance.postInitAll();
    }

}