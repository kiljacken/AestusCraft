/**
 * AestusCraft
 * 
 * ContainerInsulatedFurnace.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import dk.kiljacken.aestuscraft.tileentity.TileInsulatedFurnace;

public class ContainerInsulatedFurnace extends Container {
    public ContainerInsulatedFurnace(InventoryPlayer inventoryPlayer, TileInsulatedFurnace tileEntityInsulatedFurnace) {
        addSlotToContainer(new Slot(tileEntityInsulatedFurnace, TileInsulatedFurnace.SLOT_INPUT_1_INDEX, 61, 16));
        addSlotToContainer(new Slot(tileEntityInsulatedFurnace, TileInsulatedFurnace.SLOT_INPUT_2_INDEX, 79, 16));
        addSlotToContainer(new Slot(tileEntityInsulatedFurnace, TileInsulatedFurnace.SLOT_INPUT_3_INDEX, 97, 16));

        addSlotToContainer(new Slot(tileEntityInsulatedFurnace, TileInsulatedFurnace.SLOT_OUTPUT_1_INDEX, 61, 54));
        addSlotToContainer(new Slot(tileEntityInsulatedFurnace, TileInsulatedFurnace.SLOT_OUTPUT_2_INDEX, 79, 54));
        addSlotToContainer(new Slot(tileEntityInsulatedFurnace, TileInsulatedFurnace.SLOT_OUTPUT_3_INDEX, 97, 54));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotIndex) {
        ItemStack itemStack = null;
        Slot slot = (Slot) inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotItemStack = slot.getStack();
            itemStack = slotItemStack.copy();

            // If we're shift-clicking an item out, attempt to place it in first
            // available slot of the player's inventory
            if (slotIndex < TileInsulatedFurnace.INVENTORY_SIZE) {
                if (!this.mergeItemStack(slotItemStack, TileInsulatedFurnace.INVENTORY_SIZE, inventorySlots.size(), true)) {
                    return null;
                }
            } else {
                // Try to put the stack in the input slots
                if (!this.mergeItemStack(slotItemStack, TileInsulatedFurnace.SLOT_INPUT_1_INDEX, TileInsulatedFurnace.SLOT_INPUT_3_INDEX + 1, false)) {
                    return null;
                }
            }

            if (slotItemStack.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemStack;
    }
}
