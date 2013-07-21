/**
 * AestusCraft
 * 
 * AestusCraft.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "aec", name = "", version = "")
//@NetworkMod(clientSideRequired = true, serverSideRequired = true, channels = { "aec" }, packetHandler = PacketHandler.class)
public class AestusCraft {
    @Instance("aec")
    public static AestusCraft instance;

//    @SidedProxy(clientSide = "", serverSide = "")
//    public static CommonProxy proxy;
//
//    public static CreativeTabs creativeTab = new CreativeTabs(StringResources.CREATIVE_TAB_NAME) {
//        @Override
//        public ItemStack getIconItemStack() {
//            return new ItemStack(ModBlocks.insulatedFurnace);
//        };
//    };

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}