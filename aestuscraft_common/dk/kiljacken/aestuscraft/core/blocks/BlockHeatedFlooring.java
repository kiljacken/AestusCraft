/**
 * AestusCraft
 * 
 * BlockHeatedFlooring.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dk.kiljacken.aestuscraft.core.common.blocks.BlockBaseTile;
import dk.kiljacken.aestuscraft.core.tiles.TileHeatedFlooring;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockHeatedFlooring extends BlockBaseTile {
    @SideOnly(Side.CLIENT)
    private Icon[] m_Icons;

    @SideOnly(Side.CLIENT)
    private Icon m_InsulatedSideIcon;

    public BlockHeatedFlooring(int id)
    {
        super(id, Material.wood);

        setHardness(1.0f);
        setStepSound(Block.soundWoodFootstep);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileHeatedFlooring();
    }

    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        m_Icons = new Icon[16];
        for (int i = 0; i < 16; i++)
        {
            m_Icons[i] = iconRegister.registerIcon("aestuscraft:heated_flooring/heated_flooring_" + i);
        }

        m_InsulatedSideIcon = iconRegister.registerIcon("aestuscraft:insulated_side");
    }

    @Override
    public Icon getIcon(TileEntity tile, ForgeDirection side)
    {
        if (tile != null && tile instanceof TileHeatedFlooring && side == ForgeDirection.UP)
        {
            TileHeatedFlooring heatedFlooring = (TileHeatedFlooring) tile;

            return m_Icons[heatedFlooring.getConnectedSides()];
        }

        if (side == ForgeDirection.UP)
        {
            return m_Icons[0];
        }
        else
        {
            return m_InsulatedSideIcon;
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockId)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null && tile instanceof TileHeatedFlooring)
        {
            TileHeatedFlooring heatedFlooring = (TileHeatedFlooring) tile;

            heatedFlooring.setShouldUpdate();
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int blockId, int meta)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null && tile instanceof TileHeatedFlooring)
        {
            TileHeatedFlooring heatedFlooring = (TileHeatedFlooring) tile;

            heatedFlooring.getNetwork().split(heatedFlooring);
        }

        super.breakBlock(world, x, y, z, blockId, meta);
    }
}
