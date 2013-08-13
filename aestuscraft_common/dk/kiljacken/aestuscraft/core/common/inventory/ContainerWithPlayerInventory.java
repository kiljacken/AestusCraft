package dk.kiljacken.aestuscraft.core.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerWithPlayerInventory extends Container {
    public void addPlayerInventory(InventoryPlayer inventoryPlayer, int invOffX, int invOffY)
    {
        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, invOffX + j * 18, invOffY + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i)
        {
            addSlotToContainer(new Slot(inventoryPlayer, i, invOffX + i * 18, invOffY + 58));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }
}
