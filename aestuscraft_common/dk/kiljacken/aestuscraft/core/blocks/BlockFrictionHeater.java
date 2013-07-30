/**
 * AestusCraft
 * 
 * SubBlockFrictionHeater.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dk.kiljacken.aestuscraft.core.tiles.TileFrictionHeater;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockFrictionHeater extends BlockBaseTile {
    @SideOnly(Side.CLIENT)
    private Icon m_IconGrindstoneInactive;

    @SideOnly(Side.CLIENT)
    private Icon m_IconGrindstoneActive;

    @SideOnly(Side.CLIENT)
    private Icon m_IconStoneGear;

    @SideOnly(Side.CLIENT)
    private Icon m_IconWoodGear;

    @SideOnly(Side.CLIENT)
    private Icon m_IconInsulatedSide;
    
    public BlockFrictionHeater(int id) {
        super(id, Material.rock);
        
        setHardness(3.0f);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileFrictionHeater();
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        m_IconGrindstoneInactive = iconRegister.registerIcon("aestuscraft:friction_heater_grindstone_inactive");
        m_IconGrindstoneActive = iconRegister.registerIcon("aestuscraft:friction_heater_grindstone_active");
        m_IconStoneGear = iconRegister.registerIcon("aestuscraft:friction_heater_gear_stone");
        m_IconWoodGear = iconRegister.registerIcon("aestuscraft:friction_heater_gear_wood");
        m_IconInsulatedSide = iconRegister.registerIcon("aestuscraft:insulated_side");
    }

    @Override
    public Icon getIcon(TileEntity tile, ForgeDirection side) {
        if (tile != null && tile instanceof TileFrictionHeater) {
            TileFrictionHeater frictionHeater = (TileFrictionHeater) tile;

            ForgeDirection woodSide = frictionHeater.getOrientation();
            ForgeDirection stoneSide = woodSide.getRotation(ForgeDirection.UP);

            if (side == woodSide || side == woodSide.getOpposite()) {
                return m_IconWoodGear;
            } else if (side == stoneSide || side == stoneSide.getOpposite()) {
                return m_IconStoneGear;
            } else if (side == ForgeDirection.UP) {
                return frictionHeater.isActive() ? m_IconGrindstoneActive : m_IconGrindstoneInactive;
            }
        } else {
            if (side == ForgeDirection.SOUTH || side == ForgeDirection.NORTH) {
                return m_IconWoodGear;
            } else if (side == ForgeDirection.WEST || side == ForgeDirection.EAST) {
                return m_IconStoneGear;
            } else if (side == ForgeDirection.UP) {
                return m_IconGrindstoneInactive;
            }
        }

        return m_IconInsulatedSide;
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null && tile instanceof TileFrictionHeater) {
            TileFrictionHeater fuelBurner = (TileFrictionHeater) tile;

            if (fuelBurner.isActive()) {
                double xVel = (0.5 - random.nextDouble()) * 0.05;
                double yVel = random.nextDouble() * 0.02;
                double zVel = (0.5 - random.nextDouble()) * 0.05;

                world.spawnParticle("flame", x + 0.5, y + 1.02, z + 0.5, xVel, yVel, zVel);
            }
        }
    }
}
