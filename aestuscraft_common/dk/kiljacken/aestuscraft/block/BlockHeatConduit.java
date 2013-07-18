/**
 * AestusCraft
 * 
 * BlockHeatConduit.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dk.kiljacken.aestuscraft.api.heat.IHeatConduit;
import dk.kiljacken.aestuscraft.lib.RenderIds;
import dk.kiljacken.aestuscraft.lib.StringResources;
import dk.kiljacken.aestuscraft.tileentity.TileHeatConduit;

public class BlockHeatConduit extends BlockAECBase {
    public BlockHeatConduit(int id) {
        super(id, Material.cloth);
        setHardness(0.8F);
        setStepSound(Block.soundClothFootstep);

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

    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB boundingBox, List boundingBoxList, Entity collidingEntity) {
        setBlockBoundsBasedOnState(world, x, y, z);

        super.addCollisionBoxesToList(world, x, y, z, boundingBox, boundingBoxList, collidingEntity);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        TileHeatConduit tileHeatConduit = (TileHeatConduit) blockAccess.getBlockTileEntity(x, y, z);
        int connectedSides = tileHeatConduit.getConnectedSides();

        boolean yConnection = (connectedSides & (1 | 2)) != 0;
        boolean zConnection = (connectedSides & (4 | 8)) != 0;
        boolean xConnection = (connectedSides & (16 | 32)) != 0;

        float minY = (connectedSides & 1) != 0 ? 0.0f : zConnection | xConnection ? 0.25f : 0.3125f;
        float maxY = (connectedSides & 2) != 0 ? 1.0f : zConnection | xConnection ? 0.75f : 0.6875f;

        float minZ = (connectedSides & 4) != 0 ? 0.0f : yConnection | xConnection ? 0.25f : 0.3125f;
        float maxZ = (connectedSides & 8) != 0 ? 1.0f : yConnection | xConnection ? 0.75f : 0.6875f;

        float minX = (connectedSides & 16) != 0 ? 0.0f : yConnection | zConnection ? 0.25f : 0.3125f;
        float maxX = (connectedSides & 32) != 0 ? 1.0f : yConnection | zConnection ? 0.75f : 0.6875f;

        setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return RenderIds.heatConduit;
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
