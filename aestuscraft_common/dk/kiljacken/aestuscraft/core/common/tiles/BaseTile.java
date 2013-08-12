/**
 * AestusCraft
 * 
 * BaseTile.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import dk.kiljacken.aestuscraft.core.network.packets.PacketTileSync;
import dk.kiljacken.aestuscraft.library.nbt.NBTUtil;
import dk.kiljacken.aestuscraft.library.nbt.NBTUtil.NBTValue;
import dk.kiljacken.aestuscraft.library.nbt.handlers.ForgeDirectionNBTHandler;

public abstract class BaseTile extends TileEntity {
    @NBTValue(name = "CustomName")
    private String m_CustomName;

    @NBTValue(name = "orientation", handler = ForgeDirectionNBTHandler.class)
    private ForgeDirection m_Orientation;

    public BaseTile()
    {
        m_CustomName = "";
        m_Orientation = ForgeDirection.UNKNOWN;
    }

    /**
     * Gets the tile's custom name
     * 
     * @return The tile's custom name
     */
    public String getCustomName()
    {
        return m_CustomName;
    }

    /**
     * Sets the tile's custom name
     * 
     * @param customName
     *            The new custom name
     */
    public void setCustomName(String customName)
    {
        m_CustomName = customName;
    }

    /**
     * Queries whether the tile has a custom name
     * 
     * @return Whether the tile has a custom name
     */
    public boolean hasCustomName()
    {
        return m_CustomName != null && !m_CustomName.isEmpty();
    }

    /**
     * Gets the orientation of the tile
     * 
     * @return The orientation of the tile
     */
    public ForgeDirection getOrientation()
    {
        return m_Orientation;
    }

    /**
     * Sets the orientation of the tile
     * 
     * @param The
     *            new orientation
     */
    public void setOrientation(ForgeDirection orientation)
    {
        m_Orientation = orientation;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        NBTUtil.readAnnotatedFromNBT(this, tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        NBTUtil.writeAnnotatedToNBT(this, tagCompound);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return PacketTileSync.from(this).wrap();
    }
}
