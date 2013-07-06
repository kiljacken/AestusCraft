package dk.kiljacken.aestuscraft;

import java.io.File;

import dk.kiljacken.aestuscraft.block.ModBlocks;
import dk.kiljacken.aestuscraft.core.proxy.CommonProxy;
import dk.kiljacken.aestuscraft.lib.Reference;
import dk.kiljacken.aestuscraft.lib.StringResources;
import dk.kiljacken.aestuscraft.network.PacketHandler;
import dk.kiljacken.aestuscraft.util.ConfigurationHelper;
import dk.kiljacken.aestuscraft.util.LocalizationHelper;
import dk.kiljacken.aestuscraft.util.LogHelper;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = true, channels = { Reference.CHANNEL }, packetHandler = PacketHandler.class)
public class AestusCraft {
    @Instance(Reference.MOD_ID)
    public static AestusCraft instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        LogHelper.init();

        LocalizationHelper.init();

        ConfigurationHelper.init(new File(event.getModConfigurationDirectory(), StringResources.PATH_CONFIGURATION));

        ModBlocks.init();
    }

    @Init
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.instance().registerGuiHandler(instance, proxy);

        proxy.registerTileEntities();
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event) {

    }

}
