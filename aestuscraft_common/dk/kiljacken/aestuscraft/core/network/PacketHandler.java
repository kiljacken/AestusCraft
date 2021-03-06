package dk.kiljacken.aestuscraft.core.network;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import dk.kiljacken.aestuscraft.api.info.ModInfo;

public class PacketHandler implements IPacketHandler {
    public static final String CHANNEL = ModInfo.MOD_ID;

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        CustomPacket basePacket = CustomPacket.unwrap(packet);
        basePacket.process(manager, player);
    }
}
