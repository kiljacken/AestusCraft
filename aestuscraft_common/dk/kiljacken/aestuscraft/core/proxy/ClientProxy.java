/**
 * AestusCraft
 * 
 * ClientProxy.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.proxy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import dk.kiljacken.aestuscraft.addon.AddonLoader;
import dk.kiljacken.aestuscraft.api.heat.IHeatNetwork;
import dk.kiljacken.aestuscraft.client.renderer.item.ItemHeatConduitRenderer;
import dk.kiljacken.aestuscraft.client.renderer.tileentity.TileHeatConduitRenderer;
import dk.kiljacken.aestuscraft.heat.HeatNetwork;
import dk.kiljacken.aestuscraft.lib.BlockIds;
import dk.kiljacken.aestuscraft.lib.RenderIds;
import dk.kiljacken.aestuscraft.network.packet.PacketHeatNetworkSync;
import dk.kiljacken.aestuscraft.tileentity.TileHeatConduit;

public class ClientProxy extends CommonProxy {
    @Override
    public void handleTileUpdate(int x, int y, int z, NBTTagCompound nbtTagCompound) {
        TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getBlockTileEntity(x, y, z);

        tileEntity.readFromNBT(nbtTagCompound);
    }

    @Override
    public void syncHeatNetwork(PacketHeatNetworkSync packetHeatNetworkSync) {
        IHeatNetwork network = new HeatNetwork();

        for (int i = 0; i < packetHeatNetworkSync.numConduits; i++) {
            int xCoord = packetHeatNetworkSync.xCoords[i];
            int yCoord = packetHeatNetworkSync.yCoords[i];
            int zCoord = packetHeatNetworkSync.zCoords[i];

            TileHeatConduit conduit = (TileHeatConduit) FMLClientHandler.instance().getClient().theWorld.getBlockTileEntity(xCoord, yCoord, zCoord);

            conduit.setNetwork(network);

            if (conduit != null) {
                conduit.shouldUpdate = true;
            }
        }

        network.refresh();
    }

    @Override
    public void initializeRendering() {
        RenderIds.heatConduit = RenderingRegistry.getNextAvailableRenderId();

        ClientRegistry.bindTileEntitySpecialRenderer(TileHeatConduit.class, new TileHeatConduitRenderer());

        MinecraftForgeClient.registerItemRenderer(BlockIds.HEAT_CONDUIT, new ItemHeatConduitRenderer());

        AddonLoader.instance.initializeRenderingAll();
    }
}
