/**
 * AestusCraft
 * 
 * BlockBaseTile.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.blocks;

import java.util.Random;

import dk.kiljacken.aestuscraft.core.tiles.BaseTile;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public abstract class BlockBaseTile extends BlockContainer {
    private Random m_Random;
    
    protected BlockBaseTile(int id, Material material) {
        super(id, material);
        
        m_Random = new Random();
    }
    
    public abstract Icon getIcon(TileEntity tile, ForgeDirection side);
    
    @Override
    public Icon getIcon(int side, int meta) {
        return getIcon(null, ForgeDirection.getOrientation(side));
    }
    
    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        return getIcon(world.getBlockTileEntity(x, y, z), ForgeDirection.getOrientation(side));
    }
    
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
    public void breakBlock(World world, int x, int y, int z, int id, int meta) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity != null && tileEntity instanceof IInventory) {
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

        super.breakBlock(world, x, y, z, id, meta);
    }
}
