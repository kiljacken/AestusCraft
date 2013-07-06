package dk.kiljacken.aestuscraft.network;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import dk.kiljacken.aestuscraft.network.packet.PacketAEC;

public class PacketHandler implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        PacketAEC packetAEC = PacketType.buildPacketFrom(packet.data);
        
        packetAEC.process(manager, player);
    }

}
