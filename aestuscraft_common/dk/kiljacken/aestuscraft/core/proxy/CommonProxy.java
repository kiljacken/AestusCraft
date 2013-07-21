/**
 * AestusCraft
 * 
 * CommonProxy.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CommonProxy implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    /**
     * Registers renderers. Does nothing on server side
     */
    public void registerRenderers() {

    }

    /**
     * Initializes localization
     */
    public void initializeLocalization() {
        String[] locales = { "en_US" };

        for (String locale : locales) {
            LanguageRegistry.instance().loadLocalization("/assets/aec/lang/" + locale + ".xml", locale, true);
        }
    }

    /**
     * Synchronizes a tile to the state send by server. Does nothing on server side
     * 
     * @param x Tile's position on the X axis
     * @param y Tile's position on the Y axis
     * @param z Tile's position on the Z axis
     * @param nbt NBT data to load the tile from
     */
    public void syncTile(int x, int y, int z, NBTTagCompound nbt) {
        
    }
}
