/**
 * AestusCraft
 * 
 * TileFuelBurner.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.blocks.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.Vec3;
import dk.kiljacken.aestuscraft.api.heat.IHeatNetwork;
import dk.kiljacken.aestuscraft.core.blocks.BlockInfo;
import dk.kiljacken.aestuscraft.library.nbt.BooleanNBTHandler;
import dk.kiljacken.aestuscraft.library.nbt.ItemStackNBTHandler;
import dk.kiljacken.aestuscraft.library.nbt.NBTUtil.NBTValue;

public class TileFuelBurner extends HeatProducerBaseTile implements IInventory {
    public static final int INVENTORY_SIZE = 4;
    public static final int SLOT_QUEUE_1 = 0;
    public static final int SLOT_QUEUE_2 = 1;
    public static final int SLOT_QUEUE_3 = 2;
    public static final int SLOT_FUEL = 3;

    public static float HEAT_PER_FUEL = 1.0f;
    public static float HEAT_TRANSFER_RATE = 2.0f;

    @NBTValue(name = "inventory", handler = ItemStackNBTHandler.class)
    private ItemStack[] m_InventoryStacks;

    @NBTValue(name = "fuelTicksLeft")
    private int m_FuelTicksLeft;

    @NBTValue(name = "fuelTicks")
    private int m_FuelTicks;

    @NBTValue(name = "active", handler = BooleanNBTHandler.class)
    private boolean m_Active;

    public TileFuelBurner() {
        super(1600);

        m_InventoryStacks = new ItemStack[INVENTORY_SIZE];
        m_FuelTicks = 0;
        m_FuelTicksLeft = 0;
        m_Active = false;
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            ItemStack fuelStack = m_InventoryStacks[SLOT_FUEL];

            if (m_FuelTicksLeft == 0 && fuelStack != null && TileEntityFurnace.isItemFuel(fuelStack)) {
                m_FuelTicks = TileEntityFurnace.getItemBurnTime(fuelStack);
                m_FuelTicksLeft = m_FuelTicks;

                fuelStack.stackSize--;

                m_Active = true;
                worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlockInfo.BLOCK_PRODUCERS_ID, 0, m_Active ? 1 : 0);
                worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlockInfo.BLOCK_PRODUCERS_ID, 2, m_FuelTicks);

                if (fuelStack.stackSize <= 0) {
                    m_InventoryStacks[SLOT_FUEL] = fuelStack.getItem().getContainerItemStack(fuelStack);
                }
            }

            boolean notMoved = false;
            do {
                notMoved |= !moveQueue(SLOT_QUEUE_1, SLOT_QUEUE_2);
                notMoved |= !moveQueue(SLOT_QUEUE_2, SLOT_QUEUE_3);
                notMoved |= !moveQueue(SLOT_QUEUE_3, SLOT_FUEL);
            } while (!notMoved);

            if (m_FuelTicksLeft > 0) {
                float space = getMaxHeatLevel() - getHeatLevel();

                if (space >= HEAT_PER_FUEL) {
                    m_FuelTicksLeft--;
                    setHeatLevel(getHeatLevel() + HEAT_PER_FUEL);
                    // TODO: Limit fuel ticks events?
                    worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlockInfo.BLOCK_PRODUCERS_ID, 1, m_FuelTicksLeft);

                    if (m_FuelTicksLeft == 0) {
                        m_FuelTicks = 0;
                        m_Active = false;

                        worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlockInfo.BLOCK_PRODUCERS_ID, 0, m_Active ? 1 : 0);
                    }
                }
            }

            IHeatNetwork network = getNetwork();
            if (network != null) {
                float amount = Math.min(getHeatLevel(), HEAT_TRANSFER_RATE);

                amount = network.supplyHeat(amount);

                setHeatLevel(getHeatLevel() - amount);

                worldObj.getTotalWorldTime();
            }
        }
    }

    private boolean moveQueue(int slot1, int slot2) {
        ItemStack fuelQueue1 = m_InventoryStacks[slot1];
        ItemStack fuelQueue2 = m_InventoryStacks[slot2];

        if (fuelQueue1 != null) {
            if (fuelQueue2 == null) {
                m_InventoryStacks[slot2] = fuelQueue1;
                m_InventoryStacks[slot1] = null;

                return true;
            } else {
                if (fuelQueue1.getItem() == fuelQueue2.getItem()) {
                    int amount = Math.min(fuelQueue1.stackSize, fuelQueue2.getMaxStackSize() - fuelQueue2.stackSize);

                    fuelQueue1.stackSize -= amount;
                    fuelQueue2.stackSize += amount;

                    if (fuelQueue1.stackSize == 0) {
                        m_InventoryStacks[slot1] = null;
                    }

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean receiveClientEvent(int id, int data) {
        if (worldObj.isRemote) {
            if (id == 0) {
                m_Active = data != 0;
                worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
            } else if (id == 1) {
                m_FuelTicksLeft = data;
            } else if (id == 2) {
                m_FuelTicks = data;
            }
        }

        return true;
    }

    public boolean isActive() {
        return m_Active;
    }

    public int getScaledFuelTicksLeft(int scale) {
        return m_FuelTicks != 0 ? m_FuelTicksLeft * scale / m_FuelTicks : 0;
    }

    @Override
    public int getSizeInventory() {
        return m_InventoryStacks.length;
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

        if (itemstack != null && itemstack.stackSize > Math.min(itemstack.getMaxStackSize(), getInventoryStackLimit())) {
            itemstack.stackSize = Math.min(itemstack.getMaxStackSize(), getInventoryStackLimit());
        }
    }

    @Override
    public String getInvName() {
        return hasCustomName() ? getCustomName() : TileInfo.FUEL_BURNER_NAME;
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
        Vec3 pos = worldObj.getWorldVec3Pool().getVecFromPool(xCoord, yCoord, zCoord);

        return pos.distanceTo(entityplayer.getPosition(1.0f)) <= 8.0f;
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
