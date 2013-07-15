/**
 * AestusCraft
 * 
 * ClientProxy.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.client.FMLClientHandler;
import dk.kiljacken.aestuscraft.api.heat.IHeatNetwork;
import dk.kiljacken.aestuscraft.heat.HeatNetwork;
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

            TileHeatConduit conduit = (TileHeatConduit) Minecraft.getMinecraft().theWorld.getBlockTileEntity(xCoord, yCoord, zCoord);

            conduit.setNetwork(network);

            if (conduit != null) {
                conduit.shouldUpdate = true;
            }
        }

        network.refresh();
    }
}
