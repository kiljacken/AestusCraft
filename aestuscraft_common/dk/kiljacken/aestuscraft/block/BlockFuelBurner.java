/**
 * AestusCraft
 * 
 * BlockFuelBurner.java
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
import dk.kiljacken.aestuscraft.tileentity.TileFuelBurner;

public class BlockFuelBurner extends BlockAECBase {
    private final Random rand = new Random();

    @SideOnly(Side.CLIENT)
    private Icon m_IconIsolatedSide;

    @SideOnly(Side.CLIENT)
    private Icon m_IconFuelBurnerActive;

    public BlockFuelBurner(int id) {
        super(id, Material.rock);

        setUnlocalizedName(StringResources.FUEL_BURNER_NAME);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileFuelBurner();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        boolean active = (meta & 0x8) != 0;
        meta &= 0x7;

        return side != ForgeDirection.UP.ordinal() ? m_IconIsolatedSide : active ? m_IconFuelBurnerActive : blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon("aec:fuel_burner");
        m_IconIsolatedSide = iconRegister.registerIcon("aec:insulated_furnace_side");
        m_IconFuelBurnerActive = iconRegister.registerIcon("aec:fuel_burner_active");
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
            TileFuelBurner tileFuelBurner = (TileFuelBurner) world.getBlockTileEntity(x, y, z);

            if (tileFuelBurner != null) {
                entityPlayer.openGui(AestusCraft.instance, GuiIds.FUEL_BURNER, world, x, y, z);
            }

            return true;
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
