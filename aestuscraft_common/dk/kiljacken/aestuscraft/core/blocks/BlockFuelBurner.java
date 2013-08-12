/**
 * AestusCraft
 * 
 * SubBlockInsulatedFurnace.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dk.kiljacken.aestuscraft.AestusCraft;
import dk.kiljacken.aestuscraft.core.client.gui.GuiIds;
import dk.kiljacken.aestuscraft.core.common.blocks.BlockBaseTile;
import dk.kiljacken.aestuscraft.core.tiles.TileFuelBurner;

public class BlockFuelBurner extends BlockBaseTile {
    @SideOnly(Side.CLIENT)
    private Icon m_IconSide;

    @SideOnly(Side.CLIENT)
    private Icon m_IconActive;

    @SideOnly(Side.CLIENT)
    private Icon m_IconInactive;

    public BlockFuelBurner(int id)
    {
        super(id, Material.rock);

        setHardness(3.0f);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileFuelBurner();
    }

    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        m_IconSide = iconRegister.registerIcon("aestuscraft:insulated_side");
        m_IconActive = iconRegister.registerIcon("aestuscraft:fuel_burner_active");
        m_IconInactive = iconRegister.registerIcon("aestuscraft:fuel_burner_inactive");
    }

    @Override
    public Icon getIcon(TileEntity tile, ForgeDirection side)
    {
        if (tile != null && tile instanceof TileFuelBurner)
        {
            TileFuelBurner fuelBurner = (TileFuelBurner) tile;

            if (side == ForgeDirection.UP)
            {
                return fuelBurner.isActive() ? m_IconActive : m_IconInactive;
            }
            else
            {
                return m_IconSide;
            }
        }
        else
        {
            if (side == ForgeDirection.UP)
            {
                return m_IconInactive;
            }
            else
            {
                return m_IconSide;
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float relHitX, float relHitY, float relHitZ)
    {
        if (!world.isRemote)
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);

            if (tile != null && tile instanceof TileFuelBurner)
            {
                entityPlayer.openGui(AestusCraft.instance, GuiIds.FUEL_BURNER, world, x, y, z);
            }
        }

        return true;
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null && tile instanceof TileFuelBurner)
        {
            TileFuelBurner fuelBurner = (TileFuelBurner) tile;

            if (fuelBurner.isActive())
            {
                double xOff = 0.5 + (0.5 - random.nextDouble()) * 6 / 16;
                double zOff = 0.5 + (0.5 - random.nextDouble()) * 6 / 16;

                world.spawnParticle("smoke", x + xOff, y + 1.02, z + zOff, 0.0, 0.0, 0.0);
                world.spawnParticle("flame", x + xOff, y + 1.02, z + zOff, 0.0, 0.0, 0.0);
            }
        }
    }
}
