package dk.kiljacken.aestuscraft.buildcraft;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dk.kiljacken.aestuscraft.AestusCraft;

public class BlockEriccsonEngine extends BlockContainer {
    public BlockEriccsonEngine(int id)
    {
        super(id, Material.rock);

        setHardness(2.0f);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEricssonEngine();
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return AestusCraft.buildcraft.renderIdEricssonEngine;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        blockIcon = par1IconRegister.registerIcon("aestuscraft:insulated_side");
    }

    @Override
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile instanceof TileEricssonEngine)
        {
            return ((TileEricssonEngine) tile).getOrientation().getOpposite() == side;
        }

        return false;
    }

    @Override
    public void onPostBlockPlaced(World par1World, int par2, int par3, int par4, int par5)
    {
        TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);

        if (tile instanceof TileEricssonEngine)
        {
            ((TileEricssonEngine) tile).setOrientation(ForgeDirection.UP);
            ((TileEricssonEngine) tile).switchOrientation();
        }
    }

    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis)
    {
        TileEntity tile = worldObj.getBlockTileEntity(x, y, z);

        if (tile instanceof TileEricssonEngine)
        {
            ((TileEricssonEngine) tile).setOrientation(ForgeDirection.UP);
            return ((TileEricssonEngine) tile).switchOrientation();
        }

        return false;
    }
}
