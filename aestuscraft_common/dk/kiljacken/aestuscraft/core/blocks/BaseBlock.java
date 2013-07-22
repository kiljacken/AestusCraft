/**
 * AestusCraft
 * 
 * BaseBlock.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.blocks;

import dk.kiljacken.aestuscraft.core.blocks.tiles.BaseTile;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public abstract class BaseBlock extends BlockContainer {
    public BaseBlock(int id, Material material) {
        super(id, material);
    }
    
    @Override
    public abstract BaseTile createNewTileEntity(World world);
    
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
}
