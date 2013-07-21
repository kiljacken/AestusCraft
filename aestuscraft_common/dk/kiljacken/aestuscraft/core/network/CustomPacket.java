/**
 * AestusCraft
 * 
 * PacketAEC.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.Player;
import dk.kiljacken.aestuscraft.AestusCraft;
import dk.kiljacken.aestuscraft.library.ReflectionUtil;

public abstract class CustomPacket {
    private PacketType m_Type;

    /**
     * Writes the packet to a stream
     * 
     * @param stream The stream to write to
     * @throws IOException
     */
    public abstract void writeTo(DataOutputStream stream) throws IOException;

    /**
     * Reads the packet from a stream
     * 
     * @param stream The stream to read from
     * @throws IOException
     */
    public abstract void readFrom(DataInputStream stream) throws IOException;

    /**
     * Process the packet
     * 
     * @param manager The INetworkManager the packet was recived over
     * @param player The Player the packet was sent to
     */
    public abstract void process(INetworkManager manager, Player player);

    /**
     * Returns the packet type
     */
    public PacketType getType() {
        return m_Type;
    }

    /**
     * Wraps a packet for use with minecraft's packet system
     * 
     * @param packet The packet to wrap
     * @return The wrapped packet
     */
    public static Packet250CustomPayload wrap(CustomPacket packet) {
        try {
            ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            DataOutputStream stream = new DataOutputStream(dataStream);

            stream.writeInt(packet.getType().ordinal());
            packet.writeTo(new DataOutputStream(dataStream));

            Packet250CustomPayload wrappedPacket = new Packet250CustomPayload();
            wrappedPacket.channel = "AestusCraft";
            wrappedPacket.data = dataStream.toByteArray();

            return wrappedPacket;
        } catch (IOException e) {
            AestusCraft.log.severe("Exception while wrapping packet");
            throw new RuntimeException(e);
        }
    }

    /**
     * Unwraps a package for use with AestusCraft's packet system
     * 
     * @param wrappedPacket The packet to unwrap
     * @return The unwrapped packet
     */
    public static CustomPacket unwrap(Packet250CustomPayload wrappedPacket) {
        try {
            DataInputStream stream = new DataInputStream(new ByteArrayInputStream(wrappedPacket.data));

            PacketType type = PacketType.values()[stream.readInt()];
            CustomPacket packet = ReflectionUtil.instanciateOrCrash(type.clazz);

            packet.readFrom(stream);

            return packet;
        } catch (IOException e) {
            AestusCraft.log.severe("Exception while unwrapping packet");
            throw new RuntimeException(e);
        }
    }

    enum PacketType {
        ;

        public Class<? extends CustomPacket> clazz;

        PacketType(Class<? extends CustomPacket> clazz) {
            this.clazz = clazz;
        }
    }
}
