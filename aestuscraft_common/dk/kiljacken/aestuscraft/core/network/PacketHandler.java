/**
 * AestusCraft
 * 
 * PacketHandler.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.network;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {
    public static final String CHANNEL = "aestuscraft";

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        CustomPacket basePacket = CustomPacket.unwrap(packet);
        basePacket.process(manager, player);
    }
}
