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
import net.minecraft.item.crafting.FurnaceRecipes;
import dk.kiljacken.aestuscraft.core.tiles.TileInsulatedFurnace;

public class ContainerInsulatedFurnace extends ContainerWithPlayerInventory {
    public ContainerInsulatedFurnace(InventoryPlayer inventoryPlayer, TileInsulatedFurnace tileInsulatedFurnace) {
        addSlotToContainer(new Slot(tileInsulatedFurnace, TileInsulatedFurnace.SLOT_INPUT_1, 61, 16));
        addSlotToContainer(new Slot(tileInsulatedFurnace, TileInsulatedFurnace.SLOT_INPUT_2, 79, 16));
        addSlotToContainer(new Slot(tileInsulatedFurnace, TileInsulatedFurnace.SLOT_INPUT_3, 97, 16));
        addSlotToContainer(new Slot(tileInsulatedFurnace, TileInsulatedFurnace.SLOT_OUTPUT_1, 61, 54));
        addSlotToContainer(new Slot(tileInsulatedFurnace, TileInsulatedFurnace.SLOT_OUTPUT_2, 79, 54));
        addSlotToContainer(new Slot(tileInsulatedFurnace, TileInsulatedFurnace.SLOT_OUTPUT_3, 97, 54));

        addPlayerInventory(inventoryPlayer, 8, 84);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotIndex) {
        ItemStack itemStack = null;
        Slot slot = getSlot(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotItemStack = slot.getStack();
            itemStack = slotItemStack.copy();

            if (slotIndex < TileInsulatedFurnace.INVENTORY_SIZE) {
                // If removing from insulated furnace, move to player inventory
                if (!this.mergeItemStack(slotItemStack, TileInsulatedFurnace.INVENTORY_SIZE, inventorySlots.size(), false)) {
                    return null;
                }
            } else if (FurnaceRecipes.smelting().getSmeltingResult(slotItemStack) != null) {
                // If removing from player inventory and item is smeltable, move to insulated furnace
                if (!this.mergeItemStack(slotItemStack, TileInsulatedFurnace.SLOT_INPUT_1, TileInsulatedFurnace.SLOT_INPUT_3 + 1, true)) {
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
