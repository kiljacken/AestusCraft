/**
 * AestusCraft
 * 
 * BlockHeatConductor.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import dk.kiljacken.aestuscraft.core.blocks.tiles.TileHeatConductor;

public class BlockHeatConductor extends BlockContainer {
    public BlockHeatConductor(int id) {
        super(id, Material.cloth);

        setHardness(0.5f);
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon("aestuscraft:insulated_side");
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileHeatConductor();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockId) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null && tile instanceof TileHeatConductor) {
            TileHeatConductor heatConductor = (TileHeatConductor) tile;

            heatConductor.setShouldUpdate();
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int blockId, int meta) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null && tile instanceof TileHeatConductor) {
            TileHeatConductor heatConductor = (TileHeatConductor) tile;

            heatConductor.getNetwork().split(heatConductor);
        }

        super.breakBlock(world, x, y, z, blockId, meta);
    }
}
