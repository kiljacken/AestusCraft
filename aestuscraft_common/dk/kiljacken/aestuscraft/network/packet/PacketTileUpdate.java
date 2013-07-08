/**
 * AestusCraft
 * 
 * PacketTileUpdate.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import cpw.mods.fml.common.network.Player;
import dk.kiljacken.aestuscraft.AestusCraft;
import dk.kiljacken.aestuscraft.network.PacketType;
import dk.kiljacken.aestuscraft.util.LogHelper;
import dk.kiljacken.aestuscraft.util.StreamUtil;

public class PacketTileUpdate extends PacketAEC {
    public int x, y, z;
    public NBTTagCompound nbtTagCompound;

    public PacketTileUpdate() {
        super(PacketType.TILE_UPDATE, false);

        nbtTagCompound = new NBTTagCompound();
    }

    @Override
    public void readPacketData(DataInputStream input) throws IOException {
        x = input.readInt();
        y = input.readInt();
        z = input.readInt();

        nbtTagCompound = StreamUtil.readNBTTagCompound(input);
    }

    @Override
    public void writePacketData(DataOutputStream output) throws IOException {
        super.writePacketData(output);

        output.writeInt(x);
        output.writeInt(y);
        output.writeInt(z);

        StreamUtil.writeNBTTagCompound(nbtTagCompound, output);
    }

    @Override
    public void process(INetworkManager manager, Player player) {
        if (nbtTagCompound == null) {
            LogHelper.severe("NBTTagCompound was null when processing tile update");

            return;
        }

        AestusCraft.proxy.handleTileUpdate(x, y, z, nbtTagCompound);
    }

}
