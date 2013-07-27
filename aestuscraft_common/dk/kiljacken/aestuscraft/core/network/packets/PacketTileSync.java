/**
 * AestusCraft
 * 
 * PacketTileSync.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import cpw.mods.fml.common.network.Player;
import dk.kiljacken.aestuscraft.AestusCraft;
import dk.kiljacken.aestuscraft.core.network.CustomPacket;
import dk.kiljacken.aestuscraft.core.tiles.BaseTile;
import dk.kiljacken.aestuscraft.library.nbt.NBTUtil;

public class PacketTileSync extends CustomPacket {
    public int x, y, z;
    public NBTTagCompound nbt;

    public PacketTileSync() {
        m_Type = PacketType.PACKET_TILE_SYNC;

        nbt = new NBTTagCompound();
    }

    @Override
    public void writeTo(DataOutputStream stream) throws IOException {
        stream.writeInt(x);
        stream.writeInt(y);
        stream.writeInt(z);

        NBTUtil.writeCompoundToStream(stream, nbt);
    }

    @Override
    public void readFrom(DataInputStream stream) throws IOException {
        x = stream.readInt();
        y = stream.readInt();
        z = stream.readInt();

        nbt = NBTUtil.readCompoundFromStream(stream);
    }

    @Override
    public void process(INetworkManager manager, Player player) {
        AestusCraft.proxy.syncTile(x, y, z, nbt);
    }

    /**
     * Creates a sync packet from the given tile
     * 
     * @param tile The tile to create a packet from
     * @return The created packet
     */
    public static PacketTileSync from(BaseTile tile) {
        PacketTileSync packet = new PacketTileSync();
        packet.x = tile.xCoord;
        packet.y = tile.yCoord;
        packet.z = tile.zCoord;
        tile.writeToNBT(packet.nbt);

        return packet;
    }
}
