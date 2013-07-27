/**
 * AestusCraft
 * 
 * SubBlockTile.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.blocks.subblocks;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import dk.kiljacken.aestuscraft.core.blocks.MultipleBlock.SubBlock;
import dk.kiljacken.aestuscraft.core.tiles.BaseTile;

public abstract class SubBlockTile extends SubBlock {
    private final Random m_Random = new Random();

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public abstract TileEntity createTileEntity(World world);

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
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

        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof BaseTile) {
            BaseTile tile = (BaseTile) tileEntity;
            tile.setOrientation(direction);

            if (itemStack.hasDisplayName()) {
                tile.setCustomName(itemStack.getDisplayName());
            }
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int id) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity instanceof IInventory) {
            IInventory inventory = (IInventory) tileEntity;

            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                ItemStack itemStack = inventory.getStackInSlot(i);

                if (itemStack != null && itemStack.stackSize > 0) {
                    float dX = m_Random.nextFloat() * 0.8F + 0.1F;
                    float dY = m_Random.nextFloat() * 0.8F + 0.1F;
                    float dZ = m_Random.nextFloat() * 0.8F + 0.1F;

                    EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, itemStack.copy());

                    float factor = 0.05F;
                    entityItem.motionX = m_Random.nextGaussian() * factor;
                    entityItem.motionY = m_Random.nextGaussian() * factor + 0.2F;
                    entityItem.motionZ = m_Random.nextGaussian() * factor;
                    world.spawnEntityInWorld(entityItem);
                    itemStack.stackSize = 0;
                }
            }
        }

        super.breakBlock(world, x, y, z, id);
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int eventId, int eventData) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null) {
            return tile.receiveClientEvent(eventId, eventData);
        } else {
            return false;
        }
    }
}
