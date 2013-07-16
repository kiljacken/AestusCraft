/**
 * AestusCraft
 * 
 * BlockAECBase.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import dk.kiljacken.aestuscraft.AestusCraft;
import dk.kiljacken.aestuscraft.tileentity.TileAEC;

public abstract class BlockAECBase extends BlockContainer {

    public BlockAECBase(int id, Material material) {
        super(id, material);

        setCreativeTab(AestusCraft.creativeTab);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        super.onBlockPlacedBy(world, x, y, z, entityLivingBase, itemStack);

        ForgeDirection direction = ForgeDirection.UNKNOWN;
        int facing = MathHelper.floor_double(entityLivingBase.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        if (facing == 0) {
            direction = ForgeDirection.NORTH;
        } else if (facing == 1) {
            direction = ForgeDirection.EAST;
        } else if (facing == 2) {
            direction = ForgeDirection.SOUTH;
        } else if (facing == 3) {
            direction = ForgeDirection.WEST;
        }

        setDirectionMeta(world, x, y, z, direction);

        if (itemStack.hasDisplayName()) {
            ((TileAEC) world.getBlockTileEntity(x, y, z)).setCustomName(itemStack.getDisplayName());
        }

        ((TileAEC) world.getBlockTileEntity(x, y, z)).setOrientation(direction);
    }

    public static void setActiveMeta(World world, int x, int y, int z, boolean active) {
        int blockMeta = world.getBlockMetadata(x, y, z);

        if (active) {
            blockMeta |= 0x8;
        } else {
            blockMeta &= 0x7;
        }

        world.setBlockMetadataWithNotify(x, y, z, blockMeta, 0x1 | 0x2);
    }

    public static boolean getActiveMeta(World world, int x, int y, int z) {
        return (world.getBlockMetadata(x, y, z) & 0x8) != 0;
    }

    public static void setDirectionMeta(World world, int x, int y, int z, ForgeDirection direction) {
        int blockMeta = world.getBlockMetadata(x, y, z);

        blockMeta &= 0x8;
        blockMeta |= direction.ordinal();

        world.setBlockMetadataWithNotify(x, y, z, blockMeta, 0x1 | 0x2);
    }

    public static ForgeDirection getDirectionMeta(World world, int x, int y, int z) {
        return ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z) & 0x7);
    }
}
