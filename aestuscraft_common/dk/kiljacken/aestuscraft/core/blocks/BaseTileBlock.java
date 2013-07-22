/**
 * AestusCraft
 * 
 * BaseTileBlock.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dk.kiljacken.aestuscraft.core.blocks.tiles.BaseTile;

public abstract class BaseTileBlock extends Block {
    private final Random rand = new Random();

    public BaseTileBlock(int id, Material material) {
        super(id, material);
    }

    @Override
    public abstract BaseTile createTileEntity(World world, int metadata);

    /**
     * Gets the icon on the given side of a block
     * 
     * @param tile The BaseTile associated with the block
     * @param side The side to get icon for
     * @return The icon for the given side of the block
     */
    public abstract Icon getBlockTexture(BaseTile tile, ForgeDirection side);

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        return getBlockTexture((BaseTile) world.getBlockTileEntity(x, y, z), ForgeDirection.getOrientation(side));
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
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

        BaseTile tile = (BaseTile) world.getBlockTileEntity(x, y, z);

        tile.setOrientation(direction);

        if (itemStack.hasDisplayName()) {
            tile.setCustomName(itemStack.getDisplayName());
        }
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int id, int data) {
        super.onBlockEventReceived(world, x, y, z, id, data);

        BaseTile tile = (BaseTile) world.getBlockTileEntity(x, y, z);
        return tile != null ? tile.receiveClientEvent(id, data) : false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int id, int meta) {
        BaseTile tile = (BaseTile) world.getBlockTileEntity(x, y, z);

        if (tile instanceof IInventory) {
            IInventory inventory = (IInventory) tile;

            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                ItemStack itemStack = inventory.getStackInSlot(i);

                if (itemStack != null && itemStack.stackSize > 0) {
                    float dX = rand.nextFloat() * 0.8F + 0.1F;
                    float dY = rand.nextFloat() * 0.8F + 0.1F;
                    float dZ = rand.nextFloat() * 0.8F + 0.1F;

                    EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, itemStack.copy());

                    float factor = 0.05F;
                    entityItem.motionX = rand.nextGaussian() * factor;
                    entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                    entityItem.motionZ = rand.nextGaussian() * factor;
                    world.spawnEntityInWorld(entityItem);
                    itemStack.stackSize = 0;
                }
            }
        }

        super.breakBlock(world, x, y, z, id, meta);
    }
}
