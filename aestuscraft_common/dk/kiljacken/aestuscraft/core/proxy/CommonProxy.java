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
import cpw.mods.fml.common.registry.GameRegistry;
import dk.kiljacken.aestuscraft.gui.inventory.GuiInsulatedFurnace;
import dk.kiljacken.aestuscraft.inventory.ContainerInsulatedFurnace;
import dk.kiljacken.aestuscraft.lib.GuiIds;
import dk.kiljacken.aestuscraft.lib.StringResources;
import dk.kiljacken.aestuscraft.tileentity.TileInsulatedFurnace;
import dk.kiljacken.aestuscraft.util.LogHelper;

public class CommonProxy implements IGuiHandler {
    public void registerTileEntities() {
        LogHelper.info("Registering tile entities");

        GameRegistry.registerTileEntity(TileInsulatedFurnace.class, StringResources.TE_INSULATED_FURNACE_NAME);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GuiIds.INSULATED_FURNACE) {
            TileInsulatedFurnace tileEntityInsulatedFurnace = (TileInsulatedFurnace) world.getBlockTileEntity(x, y, z);
            return new ContainerInsulatedFurnace(player.inventory, tileEntityInsulatedFurnace);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GuiIds.INSULATED_FURNACE) {
            TileInsulatedFurnace tileEntityInsulatedFurnace = (TileInsulatedFurnace) world.getBlockTileEntity(x, y, z);
            return new GuiInsulatedFurnace(player.inventory, tileEntityInsulatedFurnace);
        }

        return null;
    }

    public void handleTileUpdate(int x, int y, int z, NBTTagCompound nbtTagCompound) {

    }
}
