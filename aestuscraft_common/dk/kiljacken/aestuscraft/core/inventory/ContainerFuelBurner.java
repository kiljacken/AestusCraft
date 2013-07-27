/**
 * AestusCraft
 * 
 * ContainerFuelBurner.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import dk.kiljacken.aestuscraft.core.tiles.TileFuelBurner;

public class ContainerFuelBurner extends ContainerWithPlayerInventory {
    public ContainerFuelBurner(InventoryPlayer inventoryPlayer, TileFuelBurner tileFuelBurner) {
        addSlotToContainer(new Slot(tileFuelBurner, TileFuelBurner.SLOT_QUEUE_1, 62, 17));
        addSlotToContainer(new Slot(tileFuelBurner, TileFuelBurner.SLOT_QUEUE_2, 62, 35));
        addSlotToContainer(new Slot(tileFuelBurner, TileFuelBurner.SLOT_QUEUE_3, 62, 53));
        addSlotToContainer(new Slot(tileFuelBurner, TileFuelBurner.SLOT_FUEL, 98, 26));
        addPlayerInventory(inventoryPlayer, 8, 84);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotIndex) {
        ItemStack itemStack = null;
        Slot slot = getSlot(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotItemStack = slot.getStack();
            itemStack = slotItemStack.copy();

            if (slotIndex < TileFuelBurner.INVENTORY_SIZE) {
                // If removing from fuel burner, move to player inventory
                if (!this.mergeItemStack(slotItemStack, TileFuelBurner.INVENTORY_SIZE, inventorySlots.size(), false)) {
                    return null;
                }
            } else if (TileEntityFurnace.isItemFuel(slotItemStack)) {
                // If removing from player inventory and item is fuel, move to fuel burner
                if (!this.mergeItemStack(slotItemStack, TileFuelBurner.SLOT_QUEUE_1, TileFuelBurner.SLOT_FUEL + 1, true)) {
                    return null;
                }
            }

            if (slotItemStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemStack;
    }
}
