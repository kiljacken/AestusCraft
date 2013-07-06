/**
 * AestusCraft
 * 
 * ClientProxy.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.proxy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.client.FMLClientHandler;

public class ClientProxy extends CommonProxy {
    @Override
    public void handleTileUpdate(int x, int y, int z, NBTTagCompound nbtTagCompound) {
        TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getBlockTileEntity(x, y, z);

        tileEntity.readFromNBT(nbtTagCompound);
    }
}
