/**
 * AestusCraft
 * 
 * PacketType.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import dk.kiljacken.aestuscraft.lib.Reference;
import dk.kiljacken.aestuscraft.network.packet.PacketAEC;
import dk.kiljacken.aestuscraft.network.packet.PacketTileUpdate;
import dk.kiljacken.aestuscraft.util.LogHelper;

public enum PacketType {
    TILE_UPDATE(PacketTileUpdate.class);

    private Class<? extends PacketAEC> clazz;

    PacketType(Class<? extends PacketAEC> clazz) {
        this.clazz = clazz;
    }

    public static PacketAEC buildPacketFrom(DataInputStream ios) {
        PacketAEC packet = null;

        try {
            int packetId = ios.readInt();

            if (packetId == -1) {
                LogHelper.severe("Tried to build packet from ended inputstream");

                return null;
            }

            PacketType[] packetTypes = values();
            if (packetId >= packetTypes.length) {
                LogHelper.severe("Tried to build packet with invalid id: " + packetId);

                return null;
            }

            packet = packetTypes[packetId].clazz.newInstance();
            packet.readPacketData(ios);
        } catch (IOException e) {
            LogHelper.severe("IOException while building packet");

            return null;
        } catch (InstantiationException | IllegalAccessException e) {
            LogHelper.severe("Error instatiating new packet");

            return null;
        }

        return packet;
    }

    public static PacketAEC buildPacketFrom(byte[] data) {
        return buildPacketFrom(new DataInputStream(new ByteArrayInputStream(data)));
    }

    public static Packet buildMCPacket(PacketAEC packet) {
        byte[] data = packet.getPacketData();

        Packet250CustomPayload packet250 = new Packet250CustomPayload();
        packet250.channel = Reference.CHANNEL;
        packet250.data = data;
        packet250.length = data.length;
        packet250.isChunkDataPacket = packet.isChunkData;

        return packet250;
    }
}
