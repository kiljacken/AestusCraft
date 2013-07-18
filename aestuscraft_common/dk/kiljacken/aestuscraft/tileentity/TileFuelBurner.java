/**
 * AestusCraft
 * 
 * TileFuelBurner.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import dk.kiljacken.aestuscraft.block.BlockAECBase;
import dk.kiljacken.aestuscraft.lib.StringResources;
import dk.kiljacken.aestuscraft.util.NBTUtil.NBTStorable;
import dk.kiljacken.aestuscraft.util.NBTUtil.NBTValue;

@NBTStorable
public class TileFuelBurner extends TileHeatProducer implements IInventory {
    public static final int INVENTORY_SIZE = 4;
    public static final int MAX_HEAT_BUFFER = 1600;
    public static final int FUEL_BURN_RATE = 1;

    public static final int SLOT_FUEL_QUEUE_1_INDEX = 0;
    public static final int SLOT_FUEL_QUEUE_2_INDEX = 1;
    public static final int SLOT_FUEL_QUEUE_3_INDEX = 2;
    public static final int SLOT_FUEL_INDEX = 3;

    @NBTValue(name = StringResources.NBT_TE_INVENTORY_ITEMS)
    private ItemStack[] m_InventoryStacks = new ItemStack[INVENTORY_SIZE];

    @NBTValue(name = StringResources.NBT_TE_FUEL_LEFT)
    private int m_FuelLeft = 0;

    @NBTValue(name = StringResources.NBT_TE_FUEL_HEAT)
    private int m_FuelHeat = 0;

    @NBTValue(name = StringResources.NBT_TE_HEAT_LEVEL)
    private int m_HeatBuffer = 0;

    @Override
    public void updateEntity() {
        ItemStack fuelStack = m_InventoryStacks[SLOT_FUEL_INDEX];

        // Consume fuel
        if (m_FuelLeft == 0 && fuelStack != null && TileEntityFurnace.isItemFuel(fuelStack)) {
            m_FuelHeat = TileEntityFurnace.getItemBurnTime(fuelStack);
            m_FuelLeft = m_FuelHeat;

            fuelStack.stackSize--;

            BlockAECBase.setActiveMeta(worldObj, xCoord, yCoord, zCoord, true);

            if (fuelStack.stackSize == 0) {
                m_InventoryStacks[SLOT_FUEL_INDEX] = fuelStack.getItem().getContainerItemStack(fuelStack);
            }
        }

        // Move queue
        moveQueue(SLOT_FUEL_QUEUE_3_INDEX, SLOT_FUEL_INDEX);
        moveQueue(SLOT_FUEL_QUEUE_2_INDEX, SLOT_FUEL_QUEUE_3_INDEX);
        moveQueue(SLOT_FUEL_QUEUE_1_INDEX, SLOT_FUEL_QUEUE_2_INDEX);

        // Burn fuel
        if (m_FuelLeft > 0) {
            int amount = Math.min(Math.min(FUEL_BURN_RATE, m_FuelLeft), MAX_HEAT_BUFFER - m_HeatBuffer);

            m_FuelLeft -= amount;
            m_HeatBuffer += amount;

            if (m_FuelLeft == 0) {
                m_FuelHeat = 0;

                BlockAECBase.setActiveMeta(worldObj, xCoord, yCoord, zCoord, false);
            }
        }

        // Supply heat to network
        if (m_HeatNetwork != null) {
            m_HeatBuffer -= m_HeatNetwork.supplyHeat(m_HeatBuffer);
        }
    }

    private void moveQueue(int slot1, int slot2) {
        ItemStack fuelQueue1 = m_InventoryStacks[slot1];
        ItemStack fuelQueue2 = m_InventoryStacks[slot2];

        if (fuelQueue1 != null) {
            if (fuelQueue2 == null) {
                m_InventoryStacks[slot2] = fuelQueue1;
                m_InventoryStacks[slot1] = null;
            } else {
                if (fuelQueue1.getItem() == fuelQueue2.getItem()) {
                    int amount = Math.min(fuelQueue1.stackSize, fuelQueue2.getMaxStackSize() - fuelQueue2.stackSize);

                    fuelQueue1.stackSize -= amount;
                    fuelQueue2.stackSize += amount;

                    if (fuelQueue1.stackSize == 0) {
                        m_InventoryStacks[slot1] = null;
                    }
                }
            }
        }
    }

    public int getFuelLeft() {
        return m_FuelLeft;
    }

    public int getFuelHeat() {
        return m_FuelHeat;
    }

    public int getHeatBuffer() {
        return m_HeatBuffer;
    }

    @Override
    public int getSizeInventory() {
        return INVENTORY_SIZE;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return m_InventoryStacks[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (m_InventoryStacks[i] != null) {
            ItemStack itemStack = m_InventoryStacks[i];

            if (itemStack.stackSize <= j) {
                m_InventoryStacks[i] = null;

                return itemStack;
            } else {
                return itemStack.splitStack(j);
            }
        }

        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        ItemStack itemStack = m_InventoryStacks[i];

        if (itemStack != null) {
            m_InventoryStacks[i] = null;
        }

        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        m_InventoryStacks[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInvName() {
        return hasCustomName() ? getCustomName() : StringResources.CONTAINER_FUEL_BURNER_NAME;
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
        return TileEntityFurnace.isItemFuel(itemstack);
    }
}
