/**
 * AestusCraft
 * 
 * BlockInsulatedFurnace.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dk.kiljacken.aestuscraft.AestusCraft;
import dk.kiljacken.aestuscraft.lib.GuiIds;
import dk.kiljacken.aestuscraft.lib.StringResources;
import dk.kiljacken.aestuscraft.tileentity.TileInsulatedFurnace;

public class BlockInsulatedFurnace extends BlockAECBase {
    private final Random rand = new Random();

    @SideOnly(Side.CLIENT)
    private Icon m_FurnaceFront;

    @SideOnly(Side.CLIENT)
    private Icon m_FurnaceFrontActive;

    public BlockInsulatedFurnace(int blockID) {
        super(blockID, Material.rock);

        setUnlocalizedName(StringResources.INSULATED_FURNACE_NAME);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileInsulatedFurnace();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        boolean active = (meta & 0x8) != 0;
        meta &= 0x7;

        if (meta == 0) {
            return side == 3 ? active ? m_FurnaceFrontActive : m_FurnaceFront : blockIcon;
        } else {
            return side == meta ? active ? m_FurnaceFrontActive : m_FurnaceFront : blockIcon;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon("aec:insulated_furnace_side");
        m_FurnaceFront = iconRegister.registerIcon("aec:insulated_furnace_front_inactive");
        m_FurnaceFrontActive = iconRegister.registerIcon("aec:insulated_furnace_front_active");
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int id, int meta) {
        dropInventory(world, x, y, z);
        super.breakBlock(world, x, y, z, id, meta);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9) {
        if (world.isRemote) {
            return true;
        } else {
            TileInsulatedFurnace tileInsulatedFurnace = (TileInsulatedFurnace) world.getBlockTileEntity(x, y, z);

            if (tileInsulatedFurnace != null) {
                entityPlayer.openGui(AestusCraft.instance, GuiIds.INSULATED_FURNACE, world, x, y, z);
            }

            return true;
        }
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        boolean active = BlockAECBase.getActiveMeta(world, x, y, z);

        if (active) {
            ForgeDirection side = getDirectionMeta(world, x, y, z);
            double xOff = 0.5;
            double yOff = 0.0 + random.nextFloat() * 6.0 / 16.0;
            double zOff = 0.5;
            double halfBlock = 0.52;
            double randOff = random.nextDouble() * 0.6 - 0.3;

            if (side == ForgeDirection.WEST || side == ForgeDirection.EAST) {
                xOff += side == ForgeDirection.WEST ? -halfBlock : halfBlock;
                zOff += randOff;
            } else if (side == ForgeDirection.NORTH || side == ForgeDirection.SOUTH) {
                xOff += randOff;
                zOff += side == ForgeDirection.WEST ? -halfBlock : halfBlock;
            }

            world.spawnParticle("smoke", x + xOff, y + yOff, z + zOff, 0.0, 0.0, 0.0);
            world.spawnParticle("flame", x + xOff, y + yOff, z + zOff, 0.0, 0.0, 0.0);
        }
    }

    private void dropInventory(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (!(tileEntity instanceof IInventory)) {
            return;
        }

        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {

            ItemStack itemStack = inventory.getStackInSlot(i);

            if (itemStack != null && itemStack.stackSize > 0) {
                float dX = rand.nextFloat() * 0.8F + 0.1F;
                float dY = rand.nextFloat() * 0.8F + 0.1F;
                float dZ = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, new ItemStack(itemStack.itemID, itemStack.stackSize, itemStack.getItemDamage()));

                if (itemStack.hasTagCompound()) {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                itemStack.stackSize = 0;
            }
        }
    }
}
