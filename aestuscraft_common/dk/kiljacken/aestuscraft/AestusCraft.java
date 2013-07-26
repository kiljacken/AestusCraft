/**
 * AestusCraft
 * 
 * AestusCraft.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft;

import java.util.logging.Logger;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import dk.kiljacken.aestuscraft.api.AestusCraftAPI;
import dk.kiljacken.aestuscraft.core.Config;
import dk.kiljacken.aestuscraft.core.Content;
import dk.kiljacken.aestuscraft.core.Registry;
import dk.kiljacken.aestuscraft.core.network.PacketHandler;
import dk.kiljacken.aestuscraft.core.proxy.CommonProxy;

@Mod(modid = "aestuscraft", name = "AestusCraft", version = "v0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = true, channels = { PacketHandler.CHANNEL }, packetHandler = PacketHandler.class)
public class AestusCraft {
    @Instance("aestuscraft")
    public static AestusCraft instance;

    @SidedProxy(clientSide = "dk.kiljacken.aestuscraft.core.proxy.ClientProxy", serverSide = "dk.kiljacken.aestuscraft.core.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static Logger log;
    public static Content content;
    public static Config config;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        AestusCraft.log = Logger.getLogger("AestusCraft");
        AestusCraft.log.setParent(FMLLog.getLogger());
        AestusCraft.config = new Config(event.getSuggestedConfigurationFile());

        Registry.initialize();
        AestusCraftAPI.initialize();

        AestusCraft.content = new Content();
        content.registerBlocks();
        content.registerItems();

        proxy.initRendering();

        NetworkRegistry.instance().registerGuiHandler(instance, proxy);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        content.registerRecipes();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}