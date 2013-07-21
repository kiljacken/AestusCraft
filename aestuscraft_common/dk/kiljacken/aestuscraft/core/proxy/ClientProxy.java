/**
 * AestusCraft
 * 
 * ClientProxy.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.proxy;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class ClientProxy extends CommonProxy {
    @Override
    public void syncTile(int x, int y, int z, NBTTagCompound nbt) {
        TileEntity tile = FMLClientHandler.instance().getClient().theWorld.getBlockTileEntity(x, y, z);
        tile.readFromNBT(nbt);
    }
}
