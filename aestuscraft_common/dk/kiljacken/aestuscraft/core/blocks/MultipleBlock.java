/**
 * AestusCraft
 * 
 * MultipleBlock.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public abstract class MultipleBlock extends Block {
    private SubBlock[] m_SubBlocks;

    public MultipleBlock(int id, Material material, SubBlock... subBlocks) {
        super(id, material);

        m_SubBlocks = subBlocks;
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        return m_SubBlocks[world.getBlockMetadata(x, y, z)].getBlockHardness(world, x, y, z);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return m_SubBlocks[metadata].hasTileEntity();
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return m_SubBlocks[metadata].createTileEntity(world);
    }

    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        return m_SubBlocks[world.getBlockMetadata(x, y, z)].getIcon(world.getBlockTileEntity(x, y, z), ForgeDirection.getOrientation(side));
    }

    @Override
    public Icon getIcon(int side, int meta) {
        return m_SubBlocks[meta].getIcon(null, ForgeDirection.getOrientation(side));
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        for (SubBlock subBlock : m_SubBlocks) {
            subBlock.registerIcons(iconRegister);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        m_SubBlocks[world.getBlockMetadata(x, y, z)].onBlockPlacedBy(world, x, y, z, entityLivingBase, itemStack);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int blockId, int meta) {
        m_SubBlocks[meta].breakBlock(world, x, y, z, blockId);
        super.breakBlock(world, x, y, z, blockId, meta);
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        m_SubBlocks[world.getBlockMetadata(x, y, z)].randomDisplayTick(world, x, y, z, random);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float relHitX, float relHitY, float relHitZ) {
        return m_SubBlocks[world.getBlockMetadata(x, y, z)].onBlockActivated(world, x, y, z, entityPlayer, side, relHitX, relHitY, relHitZ);
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int eventId, int eventData) {
        return m_SubBlocks[world.getBlockMetadata(x, y, z)].onBlockEventReceived(world, x, y, z, eventId, eventData);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(int id, CreativeTabs creativeTab, List list) {
        for (int i=0; i < m_SubBlocks.length; i++) {
            list.add(new ItemStack(id, 1, i));
        }
    }

    public static abstract class SubBlock {
        public float blockHardness;

        public SubBlock() {
        }

        public float getBlockHardness(World world, int x, int y, int z) {
            return blockHardness;
        }

        public void setBlockHardness(float blockHardness) {
            this.blockHardness = blockHardness;
        }

        public TileEntity createTileEntity(World world) {
            return null;
        }

        public boolean hasTileEntity() {
            return false;
        }

        public Icon getIcon(TileEntity tile, ForgeDirection side) {
            return null;
        }

        public void registerIcons(IconRegister iconRegister) {
        }

        public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        }

        public void breakBlock(World world, int x, int y, int z, int id) {
        }

        public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        }

        public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float relHitX, float relHitY, float relHitZ) {
            return false;
        }

        public boolean onBlockEventReceived(World world, int x, int y, int z, int eventId, int eventData) {
            return false;
        }
    }
}
