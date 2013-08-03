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
import dk.kiljacken.aestuscraft.core.tiles.TileInsulatedFurnace;

public class BlockInsulatedFurnace extends BlockBaseTile {
    @SideOnly(Side.CLIENT)
    private Icon m_IconSide;

    @SideOnly(Side.CLIENT)
    private Icon m_IconActive;

    @SideOnly(Side.CLIENT)
    private Icon m_IconInactive;

    public BlockInsulatedFurnace(int id) {
        super(id, Material.rock);

        setHardness(3.0f);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileInsulatedFurnace();
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        m_IconSide = iconRegister.registerIcon("aestuscraft:insulated_side");
        m_IconActive = iconRegister.registerIcon("aestuscraft:insulated_furnace_active");
        m_IconInactive = iconRegister.registerIcon("aestuscraft:insulated_furnace_inactive");
    }

    @Override
    public Icon getIcon(TileEntity tile, ForgeDirection side) {
        if (tile != null && tile instanceof TileInsulatedFurnace) {
            TileInsulatedFurnace insulatedFurnace = (TileInsulatedFurnace) tile;

            if (side == insulatedFurnace.getOrientation()) {
                return insulatedFurnace.isActive() ? m_IconActive : m_IconInactive;
            } else {
                return m_IconSide;
            }
        } else {
            if (side == ForgeDirection.SOUTH) {
                return m_IconInactive;
            } else {
                return m_IconSide;
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float relHitX, float relHitY, float relHitZ) {
        if (!world.isRemote) {
            TileEntity tile = world.getBlockTileEntity(x, y, z);

            if (tile != null && tile instanceof TileInsulatedFurnace) {
                entityPlayer.openGui(AestusCraft.instance, GuiIds.INSULATED_FURNACE, world, x, y, z);
            }
        }

        return true;
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null && tile instanceof TileInsulatedFurnace) {
            TileInsulatedFurnace insulatedFurnace = (TileInsulatedFurnace) tile;

            if (insulatedFurnace.isActive()) {
                ForgeDirection side = insulatedFurnace.getOrientation();
                double xOff = 0.5;
                double yOff = 0.0 + random.nextFloat() * 6.0 / 16.0;
                double zOff = 0.5;
                double halfBlock = 0.52;
                double randOff = random.nextDouble() * 0.6 - 0.3;

                if (side == ForgeDirection.WEST || side == ForgeDirection.EAST) {
                    xOff += side == ForgeDirection.WEST ? -halfBlock : halfBlock;
                    zOff += randOff;
                } else if (side == ForgeDirection.NORTH || side == ForgeDirection.SOUTH) {
                    xOff += randOff;
                    zOff += side == ForgeDirection.NORTH ? -halfBlock : halfBlock;
                }

                world.spawnParticle("smoke", x + xOff, y + yOff, z + zOff, 0.0, 0.0, 0.0);
                world.spawnParticle("flame", x + xOff, y + yOff, z + zOff, 0.0, 0.0, 0.0);
            }
        }
    }
}
