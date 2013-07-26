/**
 * AestusCraft
 * 
 * ClientProxy.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import dk.kiljacken.aestuscraft.api.heat.IHeatContainer;
import dk.kiljacken.aestuscraft.core.blocks.BlockInfo;
import dk.kiljacken.aestuscraft.core.blocks.tiles.TileFuelBurner;
import dk.kiljacken.aestuscraft.core.blocks.tiles.TileHeatConductor;
import dk.kiljacken.aestuscraft.core.blocks.tiles.TileInsulatedFurnace;
import dk.kiljacken.aestuscraft.core.client.gui.GuiFuelBurner;
import dk.kiljacken.aestuscraft.core.client.gui.GuiIds;
import dk.kiljacken.aestuscraft.core.client.gui.GuiInsulatedFurnace;
import dk.kiljacken.aestuscraft.core.client.rendering.ConductorRenderer;
import dk.kiljacken.aestuscraft.core.client.rendering.RenderIds;

public class ClientProxy extends CommonProxy {
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case GuiIds.FUEL_BURNER:
                return new GuiFuelBurner(player.inventory, (TileFuelBurner) world.getBlockTileEntity(x, y, z));
            case GuiIds.INSULATED_FURNACE:
                return new GuiInsulatedFurnace(player.inventory, (TileInsulatedFurnace) world.getBlockTileEntity(x, y, z));
            default:
                return null;
        }
    }

    @Override
    public void syncTile(int x, int y, int z, NBTTagCompound nbt) {
        TileEntity tile = FMLClientHandler.instance().getClient().theWorld.getBlockTileEntity(x, y, z);

        if (tile != null) {
            tile.readFromNBT(nbt);
        }
    }

    @Override
    public void syncTileHeatLevel(int x, int y, int z, float heatLevel) {
        TileEntity tile = FMLClientHandler.instance().getClient().theWorld.getBlockTileEntity(x, y, z);

        if (tile != null && tile instanceof IHeatContainer) {
            IHeatContainer container = (IHeatContainer) tile;

            container.setHeatLevel(Math.min(heatLevel, container.getMaxHeatLevel()));
        }
    }

    @Override
    public void initRendering() {
        RenderIds.HEAT_CONDUCTOR = RenderingRegistry.getNextAvailableRenderId();

        ClientRegistry.bindTileEntitySpecialRenderer(TileHeatConductor.class, new ConductorRenderer());
        MinecraftForgeClient.registerItemRenderer(BlockInfo.BLOCK_HEAT_CONDUCTOR_ID, new ConductorRenderer());
    }
}
