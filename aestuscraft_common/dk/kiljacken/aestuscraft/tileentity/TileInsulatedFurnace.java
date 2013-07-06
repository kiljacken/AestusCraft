package dk.kiljacken.aestuscraft.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityFurnace;

public class TileInsulatedFurnace extends TileHeatContainer implements ISidedInventory {
    public static final int INVENTORY_SIZE = 3;

    public static final int SLOT_INPUT_INDEX = 0;
    public static final int SLOT_FUEL_INDEX = 1;
    public static final int SLOT_OUTPUT_INDEX = 2;

    /**
     * Input, Fuel, Output
     */
    private ItemStack[] m_FurnaceItemStacks = new ItemStack[INVENTORY_SIZE];

    private int[] m_SlotsTop = new int[] { SLOT_INPUT_INDEX };
    private int[] m_SlotsBottom = new int[] { SLOT_FUEL_INDEX };
    private int[] m_SlotsSides = new int[] { SLOT_FUEL_INDEX, SLOT_OUTPUT_INDEX };

    private int m_HeatingLeft = 0;
    private int m_FuelLeft = 0;
    private int m_FuelHeat = 0;

    @Override
    public void updateEntity() {
        super.updateEntity();

        Minecraft.getMinecraft().mcProfiler.getClass();
        ItemStack inputStack = m_FurnaceItemStacks[SLOT_INPUT_INDEX];
        ItemStack fuelStack = m_FurnaceItemStacks[SLOT_FUEL_INDEX];
        ItemStack outputStack = m_FurnaceItemStacks[SLOT_OUTPUT_INDEX];

        // Fuel stuff
        if (m_FuelLeft == 0 && fuelStack != null && TileEntityFurnace.isItemFuel(fuelStack)) {
            m_FuelHeat = TileEntityFurnace.getItemBurnTime(fuelStack);
            m_FuelLeft = m_FuelHeat;

            fuelStack.stackSize--;

            if (fuelStack.stackSize == 0) {
                m_FurnaceItemStacks[SLOT_FUEL_INDEX] = fuelStack.getItem().getContainerItemStack(fuelStack);
            }
        }
        
        if (m_FuelLeft > 0) {
            m_FuelLeft -= addHeat(m_FuelLeft);

            if (m_FuelLeft == 0)
                m_FuelHeat = 0;

        }

        // Smelting stuff
        ItemStack smeltingResult = FurnaceRecipes.smelting().getSmeltingResult(inputStack);

        boolean spaceInOutput = true;
        if (outputStack != null && smeltingResult != null) {
            if (smeltingResult.isItemEqual(outputStack)) {
                int resultingSize = outputStack.stackSize + smeltingResult.stackSize;

                spaceInOutput = resultingSize <= getInventoryStackLimit() && resultingSize <= outputStack.getMaxStackSize();
            }
        }

        boolean canSmelt = smeltingResult != null && (spaceInOutput || outputStack == null);

        if (m_HeatingLeft == 0 && canSmelt) {
            m_HeatingLeft = 200;
        }
        
        if (m_HeatingLeft > 0 && canSmelt) {
            m_HeatingLeft -= removeHeat(m_HeatingLeft);

            if (m_HeatingLeft == 0) {
                inputStack.stackSize--;

                if (inputStack.stackSize == 0)
                    m_FurnaceItemStacks[SLOT_INPUT_INDEX] = null;

                if (outputStack != null)
                    outputStack.stackSize += smeltingResult.stackSize;
                else
                    m_FurnaceItemStacks[SLOT_OUTPUT_INDEX] = smeltingResult.copy();
            }
        }
    }

    @Override
    public int getHeatLoss() {
        return 0;
    }

    @Override
    public int getHeatConductivity() {
        return 1;
    }

    public int getHeatingLeft() {
        return m_HeatingLeft;
    }

    public int getFuelLeft() {
        return m_FuelLeft;
    }

    public int getFuelHeat() {
        return m_FuelHeat;
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

        if (itemStack != null)
            m_FurnaceItemStacks[i] = null;

        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        m_FurnaceItemStacks[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
            itemstack.stackSize = getInventoryStackLimit();
    }

    @Override
    public String getInvName() {
        return hasCustomName() ? getCustomName() : "container.insulatedFurnace";
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
        boolean playerInRange = entityplayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64.0D;

        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this && playerInRange;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack) {
        return i == SLOT_OUTPUT_INDEX ? false : (i == SLOT_FUEL_INDEX ? TileEntityFurnace.isItemFuel(itemstack) : true);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return side == 0 ? m_SlotsBottom : (side == 1 ? m_SlotsTop : m_SlotsSides);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return isStackValidForSlot(i, itemstack);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return j != 0 || i != SLOT_FUEL_INDEX || itemstack.itemID == Item.bucketEmpty.itemID;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        NBTTagList itemStacksTag = nbtTagCompound.getTagList("items");
        m_FurnaceItemStacks = new ItemStack[getSizeInventory()];

        for (int i = 0; i < itemStacksTag.tagCount(); i++) {
            NBTTagCompound itemStackTag = (NBTTagCompound) itemStacksTag.tagAt(i);

            byte slot = itemStackTag.getByte("slot");

            if (slot >= 0 && slot < m_FurnaceItemStacks.length) {
                m_FurnaceItemStacks[slot] = ItemStack.loadItemStackFromNBT(itemStackTag);
            }
        }

        m_FuelLeft = nbtTagCompound.getInteger("fuelLeft");
        m_FuelHeat = nbtTagCompound.getInteger("fuelHeat");
        m_HeatingLeft = nbtTagCompound.getInteger("heatingLeft");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        NBTTagList itemStacksTag = new NBTTagList();

        for (int i = 0; i < m_FurnaceItemStacks.length; i++) {
            if (m_FurnaceItemStacks[i] != null) {
                NBTTagCompound itemStackTag = new NBTTagCompound();

                itemStackTag.setByte("slot", (byte) i);
                m_FurnaceItemStacks[i].writeToNBT(itemStackTag);

                itemStacksTag.appendTag(itemStackTag);
            }
        }

        nbtTagCompound.setTag("items", itemStacksTag);
        nbtTagCompound.setInteger("fuelLeft", m_FuelLeft);
        nbtTagCompound.setInteger("fuelHeat", m_FuelHeat);
        nbtTagCompound.setInteger("heatingLeft", m_HeatingLeft);
    }
}
