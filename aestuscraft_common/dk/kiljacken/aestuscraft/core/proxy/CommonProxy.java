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
import dk.kiljacken.aestuscraft.addon.AddonLoader;
import dk.kiljacken.aestuscraft.gui.inventory.GuiFuelBurner;
import dk.kiljacken.aestuscraft.gui.inventory.GuiInsulatedFurnace;
import dk.kiljacken.aestuscraft.inventory.ContainerFuelBurner;
import dk.kiljacken.aestuscraft.inventory.ContainerInsulatedFurnace;
import dk.kiljacken.aestuscraft.lib.GuiIds;
import dk.kiljacken.aestuscraft.lib.StringResources;
import dk.kiljacken.aestuscraft.network.packet.PacketHeatNetworkSync;
import dk.kiljacken.aestuscraft.tileentity.TileFuelBurner;
import dk.kiljacken.aestuscraft.tileentity.TileHeatConduit;
import dk.kiljacken.aestuscraft.tileentity.TileInsulatedFurnace;
import dk.kiljacken.aestuscraft.util.LogHelper;

public class CommonProxy implements IGuiHandler {
    public void registerTileEntities() {
        LogHelper.info("Registering tile entities");

        GameRegistry.registerTileEntity(TileInsulatedFurnace.class, StringResources.TE_INSULATED_FURNACE_NAME);
        GameRegistry.registerTileEntity(TileHeatConduit.class, StringResources.TE_HEAT_CONDUIT_NAME);
        GameRegistry.registerTileEntity(TileFuelBurner.class, StringResources.TE_FUEL_BURNER_NAME);

        AddonLoader.instance.registerAllTileEntites();
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == GuiIds.INSULATED_FURNACE) {
            TileInsulatedFurnace tileInsulatedFurnace = (TileInsulatedFurnace) world.getBlockTileEntity(x, y, z);
            return new ContainerInsulatedFurnace(player.inventory, tileInsulatedFurnace);
        } else if (id == GuiIds.FUEL_BURNER) {
            TileFuelBurner tileFuelBurner = (TileFuelBurner) world.getBlockTileEntity(x, y, z);
            return new ContainerFuelBurner(player.inventory, tileFuelBurner);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == GuiIds.INSULATED_FURNACE) {
            TileInsulatedFurnace tileInsulatedFurnace = (TileInsulatedFurnace) world.getBlockTileEntity(x, y, z);
            return new GuiInsulatedFurnace(player.inventory, tileInsulatedFurnace);
        } else if (id == GuiIds.FUEL_BURNER) {
            TileFuelBurner tileFuelBurner = (TileFuelBurner) world.getBlockTileEntity(x, y, z);
            return new GuiFuelBurner(player.inventory, tileFuelBurner);
        }

        return null;
    }

    public void initializeRendering() {

    }

    public void handleTileUpdate(int x, int y, int z, NBTTagCompound nbtTagCompound) {

    }

    public void syncHeatNetwork(PacketHeatNetworkSync packetHeatNetworkSync) {

    }
}
