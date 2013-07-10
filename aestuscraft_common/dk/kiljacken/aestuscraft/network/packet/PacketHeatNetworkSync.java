/**
 * AestusCraft
 * 
 * PacketHeatNetworkSync.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.Player;
import dk.kiljacken.aestuscraft.AestusCraft;
import dk.kiljacken.aestuscraft.api.heat.IHeatConduit;
import dk.kiljacken.aestuscraft.api.heat.IHeatNetwork;
import dk.kiljacken.aestuscraft.network.PacketType;

public class PacketHeatNetworkSync extends PacketAEC {
    public int numConduits;
    public int[] xCoords;
    public int[] yCoords;
    public int[] zCoords;

    public PacketHeatNetworkSync() {
        super(PacketType.HEAT_NETWORK_SYNC, false);
    }

    public void fillFrom(IHeatNetwork network) {
        Set<IHeatConduit> conduits = network.getHeatConduits();

        numConduits = conduits.size();

        xCoords = new int[numConduits];
        yCoords = new int[numConduits];
        zCoords = new int[numConduits];

        int i = 0;
        for (IHeatConduit conduit : conduits) {
            TileEntity tileConduit = (TileEntity) conduit;

            xCoords[i] = tileConduit.xCoord;
            yCoords[i] = tileConduit.yCoord;
            zCoords[i] = tileConduit.zCoord;

            i++;
        }
    }

    @Override
    public void readPacketData(DataInputStream input) throws IOException {
        numConduits = input.readInt();

        xCoords = new int[numConduits];
        yCoords = new int[numConduits];
        zCoords = new int[numConduits];

        for (int i = 0; i < numConduits; i++) {
            xCoords[i] = input.readInt();
            yCoords[i] = input.readInt();
            zCoords[i] = input.readInt();
        }
    }

    @Override
    public void writePacketData(DataOutputStream output) throws IOException {
        super.writePacketData(output);

        output.writeInt(numConduits);

        for (int i = 0; i < numConduits; i++) {
            output.writeInt(xCoords[i]);
            output.writeInt(yCoords[i]);
            output.writeInt(zCoords[i]);
        }
    }

    @Override
    public void process(INetworkManager manager, Player player) {
        AestusCraft.proxy.syncHeatNetwork(this);

    }

}
