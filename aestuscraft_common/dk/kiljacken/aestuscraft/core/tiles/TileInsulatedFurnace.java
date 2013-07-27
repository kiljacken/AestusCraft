/**
 * AestusCraft
 * 
 * TileInsulatedFurnace.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.Vec3;
import dk.kiljacken.aestuscraft.core.blocks.BlockInfo;
import dk.kiljacken.aestuscraft.library.nbt.BooleanNBTHandler;
import dk.kiljacken.aestuscraft.library.nbt.ItemStackNBTHandler;
import dk.kiljacken.aestuscraft.library.nbt.NBTUtil.NBTValue;

public class TileInsulatedFurnace extends HeatConsumerBaseTile implements IInventory {
    public static final int INVENTORY_SIZE = 6;
    public static final int SLOT_INPUT_1 = 0;
    public static final int SLOT_INPUT_2 = 1;
    public static final int SLOT_INPUT_3 = 2;
    public static final int SLOT_OUTPUT_1 = 3;
    public static final int SLOT_OUTPUT_2 = 4;
    public static final int SLOT_OUTPUT_3 = 5;

    public static float HEAT_PER_BURN_TICK = 2.0f;

    @NBTValue(name = "inventory", handler = ItemStackNBTHandler.class)
    private ItemStack[] m_InventoryStacks;

    @NBTValue(name = "burnTicksLeft")
    private int m_BurnTicksLeft;

    @NBTValue(name = "active", handler = BooleanNBTHandler.class)
    private boolean m_Active;

    public TileInsulatedFurnace() {
        super(12800);

        m_InventoryStacks = new ItemStack[INVENTORY_SIZE];
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            boolean canSmelt1 = smeltableInSlot(SLOT_INPUT_1) && spaceInSlot(SLOT_INPUT_1, SLOT_OUTPUT_1);
            boolean canSmelt2 = smeltableInSlot(SLOT_INPUT_2) && spaceInSlot(SLOT_INPUT_2, SLOT_OUTPUT_2);
            boolean canSmelt3 = smeltableInSlot(SLOT_INPUT_3) && spaceInSlot(SLOT_INPUT_3, SLOT_OUTPUT_3);
            boolean canSmelt = canSmelt1 | canSmelt2 | canSmelt3;

            if (m_BurnTicksLeft == 0 && canSmelt) {
                m_BurnTicksLeft = 200;
                m_Active = true;
                worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlockInfo.BLOCK_CONSUMERS_ID, 0, m_Active ? 1 : 0);
            } else if (m_BurnTicksLeft > 0 && !canSmelt) {
                m_BurnTicksLeft = 0;
                m_Active = false;
                worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlockInfo.BLOCK_CONSUMERS_ID, 0, m_Active ? 1 : 0);
            }

            if (m_BurnTicksLeft > 0 && getHeatLevel() > HEAT_PER_BURN_TICK) {
                setHeatLevel(getHeatLevel() - HEAT_PER_BURN_TICK);
                m_BurnTicksLeft--;
                // TODO: Limit burn ticks events?
                worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlockInfo.BLOCK_CONSUMERS_ID, 1, m_BurnTicksLeft);

                if (m_BurnTicksLeft == 0) {
                    if (canSmelt1) {
                        doSmelting(SLOT_INPUT_1, SLOT_OUTPUT_1);
                        canSmelt1 = smeltableInSlot(SLOT_INPUT_1) && spaceInSlot(SLOT_INPUT_1, SLOT_OUTPUT_1);
                    }

                    if (canSmelt2) {
                        doSmelting(SLOT_INPUT_2, SLOT_OUTPUT_2);
                        canSmelt2 = smeltableInSlot(SLOT_INPUT_2) && spaceInSlot(SLOT_INPUT_2, SLOT_OUTPUT_2);
                    }

                    if (canSmelt3) {
                        doSmelting(SLOT_INPUT_3, SLOT_OUTPUT_3);
                        canSmelt3 = smeltableInSlot(SLOT_INPUT_3) && spaceInSlot(SLOT_INPUT_3, SLOT_OUTPUT_3);
                    }

                    canSmelt = canSmelt1 | canSmelt2 | canSmelt3;
                    if (!canSmelt) {
                        m_Active = false;
                        worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlockInfo.BLOCK_CONSUMERS_ID, 0, m_Active ? 1 : 0);
                    }
                }
            }
        }
    }

    private boolean smeltableInSlot(int slot) {
        return FurnaceRecipes.smelting().getSmeltingResult(m_InventoryStacks[slot]) != null;
    }

    private boolean spaceInSlot(int inputSlot, int outputSlot) {
        ItemStack input = FurnaceRecipes.smelting().getSmeltingResult(m_InventoryStacks[inputSlot]);
        ItemStack output = m_InventoryStacks[outputSlot];

        if (output != null) {
            if (input.isItemEqual(output)) {
                return input.stackSize < Math.min(getInventoryStackLimit(), output.getMaxStackSize()) - output.stackSize;
            }
        }

        return true;
    }

    private void doSmelting(int inputSlot, int outputSlot) {
        ItemStack smeltingResult = FurnaceRecipes.smelting().getSmeltingResult(m_InventoryStacks[inputSlot]);
        ItemStack output = m_InventoryStacks[outputSlot];

        decrStackSize(inputSlot, 1);

        if (output != null) {
            output.stackSize += smeltingResult.stackSize;
        } else {
            m_InventoryStacks[outputSlot] = smeltingResult.copy();
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int data) {
        if (worldObj.isRemote) {
            if (id == 0) {
                m_Active = data != 0;
                worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
            } else if (id == 1) {
                m_BurnTicksLeft = data;
            }
        }

        return true;
    }

    public boolean isActive() {
        return m_Active;
    }

    public int getScaledBurnTicksLeft(int scale) {
        return m_BurnTicksLeft * scale / 200;
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
        return hasCustomName() ? getCustomName() : TileInfo.INSULATED_FURNACE_NAME;
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
        if (i < SLOT_OUTPUT_1) {
            return FurnaceRecipes.smelting().getSmeltingResult(itemstack) != null;
        } else {
            return false;
        }
    }
}
