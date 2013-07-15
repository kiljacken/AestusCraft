/**
 * AestusCraft
 * 
 * TileInsulatedFurnace.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import dk.kiljacken.aestuscraft.block.BlockAECBase;
import dk.kiljacken.aestuscraft.lib.StringResources;

public class TileInsulatedFurnace extends TileBoundedHeatConsumer implements ISidedInventory {
    public static final int INVENTORY_SIZE = 6;

    public static final int HEAT_CONSUMPTION_RATE = 2;

    public static final int SLOT_INPUT_1_INDEX = 0;
    public static final int SLOT_INPUT_2_INDEX = 1;
    public static final int SLOT_INPUT_3_INDEX = 2;
    public static final int SLOT_OUTPUT_1_INDEX = 3;
    public static final int SLOT_OUTPUT_2_INDEX = 4;
    public static final int SLOT_OUTPUT_3_INDEX = 5;

    private final int[] m_Slots = new int[] { SLOT_INPUT_1_INDEX, SLOT_INPUT_2_INDEX, SLOT_INPUT_3_INDEX, SLOT_OUTPUT_1_INDEX, SLOT_OUTPUT_2_INDEX, SLOT_OUTPUT_3_INDEX };

    private ItemStack[] m_FurnaceItemStacks = new ItemStack[INVENTORY_SIZE];

    private int m_HeatingLeft = 0;

    public TileInsulatedFurnace() {
        super(12800);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        // Smelting stuff
        boolean canSmelt1 = smeltableInSlot(SLOT_INPUT_1_INDEX) && spaceInSlot(SLOT_OUTPUT_1_INDEX, SLOT_INPUT_1_INDEX);
        boolean canSmelt2 = smeltableInSlot(SLOT_INPUT_2_INDEX) && spaceInSlot(SLOT_OUTPUT_1_INDEX, SLOT_INPUT_2_INDEX);
        boolean canSmelt3 = smeltableInSlot(SLOT_INPUT_3_INDEX) && spaceInSlot(SLOT_OUTPUT_1_INDEX, SLOT_INPUT_3_INDEX);

        boolean canSmelt = canSmelt1 || canSmelt2 || canSmelt3;

        if (m_HeatingLeft == 0 && canSmelt) {
            m_HeatingLeft = 200;

            BlockAECBase.setActiveMeta(worldObj, xCoord, yCoord, zCoord, true);
        } else if (m_HeatingLeft > 0 && !canSmelt) {
            m_HeatingLeft = 0;

            BlockAECBase.setActiveMeta(worldObj, xCoord, yCoord, zCoord, false);
        }

        if (m_HeatingLeft == 0 && !canSmelt) {
            BlockAECBase.setActiveMeta(worldObj, xCoord, yCoord, zCoord, false);
        }

        if (m_HeatingLeft > 0) {
            int removedHeat = removeHeat(HEAT_CONSUMPTION_RATE);

            if (removedHeat == 2) {
                m_HeatingLeft--;
            } else {
                supplyHeat(removedHeat);
            }

            if (m_HeatingLeft == 0) {
                if (canSmelt1) {
                    doSmelting(SLOT_INPUT_1_INDEX, SLOT_OUTPUT_1_INDEX);
                }

                if (canSmelt2) {
                    doSmelting(SLOT_INPUT_2_INDEX, SLOT_OUTPUT_2_INDEX);
                }

                if (canSmelt3) {
                    doSmelting(SLOT_INPUT_3_INDEX, SLOT_OUTPUT_3_INDEX);
                }
            }
        }
    }

    private boolean smeltableInSlot(int slot) {
        ItemStack stack = m_FurnaceItemStacks[slot];

        if (stack == null) {
            return false;
        } else {
            ItemStack smeltingResult = FurnaceRecipes.smelting().getSmeltingResult(stack);

            return smeltingResult != null;
        }
    }

    private boolean spaceInSlot(int dstSlot, int srcSlot) {
        ItemStack checkedStack = m_FurnaceItemStacks[dstSlot];
        ItemStack stack = FurnaceRecipes.smelting().getSmeltingResult(m_FurnaceItemStacks[srcSlot]);

        if (checkedStack != null && stack != null) {
            if (stack.isItemEqual(checkedStack)) {
                int resultingSize = checkedStack.stackSize + stack.stackSize;

                return resultingSize <= getInventoryStackLimit() && resultingSize <= checkedStack.getMaxStackSize();
            }
        }

        return checkedStack == null;
    }

    private void doSmelting(int inputSlot, int outputSlot) {
        ItemStack inputStack = m_FurnaceItemStacks[inputSlot];
        ItemStack outputStack = m_FurnaceItemStacks[outputSlot];
        ItemStack smeltingResult = FurnaceRecipes.smelting().getSmeltingResult(inputStack);

        inputStack.stackSize--;

        if (inputStack.stackSize == 0) {
            m_FurnaceItemStacks[inputSlot] = null;
        }

        if (outputStack != null) {
            outputStack.stackSize += smeltingResult.stackSize;
        } else {
            m_FurnaceItemStacks[outputSlot] = smeltingResult.copy();
        }
    }

    public int getHeatingLeft() {
        return m_HeatingLeft;
    }

    @Override
    public int getSizeInventory() {
        return INVENTORY_SIZE;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return m_FurnaceItemStacks[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (m_FurnaceItemStacks[i] != null) {
            ItemStack itemStack = m_FurnaceItemStacks[i];

            if (itemStack.stackSize <= j) {
                m_FurnaceItemStacks[i] = null;

                return itemStack;
            } else {
                return itemStack.splitStack(j);
            }
        }

        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        ItemStack itemStack = m_FurnaceItemStacks[i];

        if (itemStack != null) {
            m_FurnaceItemStacks[i] = null;
        }

        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        m_FurnaceItemStacks[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInvName() {
        return hasCustomName() ? getCustomName() : StringResources.CONTAINER_INSULATED_FURNACE_NAME;
    }

    @Override
    public boolean isInvNameLocalized() {
        return hasCustomName();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        boolean playerInRange = entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;

        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this && playerInRange;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return m_Slots;
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return i != SLOT_OUTPUT_1_INDEX && i != SLOT_OUTPUT_2_INDEX && i != SLOT_OUTPUT_3_INDEX;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return i != SLOT_INPUT_1_INDEX && i != SLOT_INPUT_2_INDEX && i != SLOT_INPUT_3_INDEX;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        NBTTagList itemStacksTag = nbtTagCompound.getTagList(StringResources.NBT_TE_INVENTORY_ITEMS);
        m_FurnaceItemStacks = new ItemStack[getSizeInventory()];

        for (int i = 0; i < itemStacksTag.tagCount(); i++) {
            NBTTagCompound itemStackTag = (NBTTagCompound) itemStacksTag.tagAt(i);

            byte slot = itemStackTag.getByte(StringResources.NBT_TE_INVENTORY_SLOT);

            if (slot >= 0 && slot < m_FurnaceItemStacks.length) {
                m_FurnaceItemStacks[slot] = ItemStack.loadItemStackFromNBT(itemStackTag);
            }
        }

        m_HeatingLeft = nbtTagCompound.getInteger(StringResources.NBT_TE_HEATING_LEFT);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        NBTTagList itemStacksTag = new NBTTagList();

        for (int i = 0; i < m_FurnaceItemStacks.length; i++) {
            if (m_FurnaceItemStacks[i] != null) {
                NBTTagCompound itemStackTag = new NBTTagCompound();

                itemStackTag.setByte(StringResources.NBT_TE_INVENTORY_SLOT, (byte) i);
                m_FurnaceItemStacks[i].writeToNBT(itemStackTag);

                itemStacksTag.appendTag(itemStackTag);
            }
        }

        nbtTagCompound.setTag(StringResources.NBT_TE_INVENTORY_ITEMS, itemStacksTag);
        nbtTagCompound.setInteger(StringResources.NBT_TE_HEATING_LEFT, m_HeatingLeft);
    }
}
