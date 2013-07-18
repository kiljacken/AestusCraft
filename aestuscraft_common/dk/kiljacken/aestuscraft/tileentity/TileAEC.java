/**
 * AestusCraft
 * 
 * TileAEC.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import dk.kiljacken.aestuscraft.lib.StringResources;
import dk.kiljacken.aestuscraft.network.PacketType;
import dk.kiljacken.aestuscraft.network.packet.PacketTileUpdate;
import dk.kiljacken.aestuscraft.util.NBTUtil;
import dk.kiljacken.aestuscraft.util.NBTUtil.NBTStorable;
import dk.kiljacken.aestuscraft.util.NBTUtil.NBTValue;

@NBTStorable
public class TileAEC extends TileEntity {
    @NBTValue(name = StringResources.NBT_TE_CUSTOM_NAME)
    private String m_CustomName;

    @NBTValue(name = StringResources.NBT_TE_ORIENTATION)
    private int m_Direction;

    public TileAEC() {
        m_CustomName = "";
        m_Direction = ForgeDirection.NORTH.ordinal();
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

    public void setOrientation(ForgeDirection direction) {
        m_Direction = direction.ordinal();
    }

    public ForgeDirection getOrientation() {
        return ForgeDirection.getOrientation(m_Direction);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        NBTUtil.readStorableFromNBT(this, nbtTagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        NBTUtil.writeStorableToNBT(this, nbtTagCompound);
    }

    @Override
    public Packet getDescriptionPacket() {
        PacketTileUpdate packet = new PacketTileUpdate();

        packet.x = xCoord;
        packet.y = yCoord;
        packet.z = zCoord;
        writeToNBT(packet.nbtTagCompound);

        return PacketType.buildMCPacket(packet);
    }
}
