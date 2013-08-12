/**
 * AestusCraft
 * 
 * PacketHeatLevelSync.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.Player;
import dk.kiljacken.aestuscraft.AestusCraft;
import dk.kiljacken.aestuscraft.api.heat.IHeatContainer;
import dk.kiljacken.aestuscraft.core.network.CustomPacket;

public class PacketHeatLevelSync extends CustomPacket {
    public int x, y, z;
    public float heatLevel;

    public PacketHeatLevelSync()
    {
        m_Type = PacketType.PACKET_HEAT_LEVEL_SYNC;
    }

    @Override
    public void writeTo(DataOutputStream stream) throws IOException
    {
        stream.writeInt(x);
        stream.writeInt(y);
        stream.writeInt(z);
        stream.writeFloat(heatLevel);
    }

    @Override
    public void readFrom(DataInputStream stream) throws IOException
    {
        x = stream.readInt();
        y = stream.readInt();
        z = stream.readInt();
        heatLevel = stream.readFloat();
    }

    @Override
    public void process(INetworkManager manager, Player player)
    {
        AestusCraft.proxy.syncTileHeatLevel(x, y, z, heatLevel);
    }

    public static PacketHeatLevelSync from(TileEntity tile)
    {
        if (tile instanceof IHeatContainer)
        {
            IHeatContainer container = (IHeatContainer) tile;

            PacketHeatLevelSync packet = new PacketHeatLevelSync();
            packet.x = tile.xCoord;
            packet.y = tile.yCoord;
            packet.z = tile.zCoord;
            packet.heatLevel = container.getHeatLevel();

            return packet;
        }

        return null;
    }
}
