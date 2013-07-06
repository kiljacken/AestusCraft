package dk.kiljacken.aestuscraft.tileentity;

import dk.kiljacken.aestuscraft.lib.StringResources;
import dk.kiljacken.aestuscraft.network.PacketType;
import dk.kiljacken.aestuscraft.network.packet.PacketTileUpdate;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileAEC extends TileEntity {
    private String m_CustomName;
    private ForgeDirection m_Direction;

    public TileAEC() {
        m_CustomName = "";
        m_Direction = ForgeDirection.NORTH;
    }

    public String getCustomName() {
        return m_CustomName;
    }

    public void setCustomName(String customName) {
        m_CustomName = customName;
    }

    public boolean hasCustomName() {
        return getCustomName() != null && getCustomName().length() > 0;
    }

    public void setOrientation(int direction) {
        m_Direction = ForgeDirection.getOrientation(direction);
    }

    public ForgeDirection getOrientation() {
        return m_Direction;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        if (nbtTagCompound.hasKey(StringResources.NBT_TE_CUSTOM_NAME))
            setCustomName(nbtTagCompound.getString(StringResources.NBT_TE_CUSTOM_NAME));

        setOrientation(nbtTagCompound.getByte(StringResources.NBT_TE_ORIENTATION));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        if (hasCustomName())
            nbtTagCompound.setString(StringResources.NBT_TE_CUSTOM_NAME, getCustomName());

        nbtTagCompound.setByte(StringResources.NBT_TE_ORIENTATION, (byte) getOrientation().ordinal());
    }

    @Override
    public Packet getDescriptionPacket() {
        PacketTileUpdate packet = new PacketTileUpdate();
        
        packet.x = this.xCoord;
        packet.y = this.yCoord;
        packet.z = this.zCoord;
        
        packet.nbtTagCompound = new NBTTagCompound();
        writeToNBT(packet.nbtTagCompound);
        
        return PacketType.buildMCPacket(packet);
    }
}
