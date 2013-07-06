/**
 * AestusCraft
 * 
 * PacketAEC.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.network.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.INetworkManager;
import cpw.mods.fml.common.network.Player;
import dk.kiljacken.aestuscraft.network.PacketType;
import dk.kiljacken.aestuscraft.util.LogHelper;

public abstract class PacketAEC {
    public PacketType type;
    public boolean isChunkData;

    public PacketAEC(PacketType type, boolean isChunkData) {
        this.type = type;
        this.isChunkData = isChunkData;
    }

    public byte[] getPacketData() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            writePacketData(new DataOutputStream(output));
        } catch (IOException e) {
            LogHelper.severe("IOException while writing packet data");

            return null;
        }

        return output.toByteArray();
    }

    public abstract void readPacketData(DataInputStream input) throws IOException;

    public abstract void writePacketData(DataOutputStream output) throws IOException;

    public abstract void process(INetworkManager manager, Player player);
}
