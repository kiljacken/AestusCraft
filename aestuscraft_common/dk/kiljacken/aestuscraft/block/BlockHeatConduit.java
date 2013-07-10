/**
 * AestusCraft
 * 
 * BlockHeatConduit.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dk.kiljacken.aestuscraft.api.heat.IHeatConduit;
import dk.kiljacken.aestuscraft.lib.StringResources;
import dk.kiljacken.aestuscraft.tileentity.TileHeatConduit;

public class BlockHeatConduit extends BlockAECBase {
    public BlockHeatConduit(int id) {
        super(id, Material.cloth);

        setUnlocalizedName(StringResources.HEAT_CONDUIT_NAME);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        IHeatConduit heatConduit = (IHeatConduit) world.getBlockTileEntity(x, y, z);
        heatConduit.getNetwork().split(heatConduit);

        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        super.onNeighborBlockChange(world, x, y, z, blockId);

        TileHeatConduit tile = (TileHeatConduit) world.getBlockTileEntity(x, y, z);
        tile.shouldUpdate = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon("aec:insulated_furnace_side");
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileHeatConduit();
    }
}
