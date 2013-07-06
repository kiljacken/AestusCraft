package dk.kiljacken.aestuscraft.core.proxy;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class ClientProxy extends CommonProxy {
    @Override
    public void handleTileUpdate(int x, int y, int z, NBTTagCompound nbtTagCompound) {
        TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getBlockTileEntity(x, y, z);
        
        tileEntity.readFromNBT(nbtTagCompound);
    }
}
