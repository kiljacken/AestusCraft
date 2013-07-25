/**
 * AestusCraft
 * 
 * ItemStackNBTHandler.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.library.nbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemStackNBTHandler implements INBTHandler {
    @SuppressWarnings("unchecked")
    @Override
    public <T> T readFromTag(NBTBase tag) {
        if (tag instanceof NBTTagCompound) {
            return (T) ItemStack.loadItemStackFromNBT((NBTTagCompound) tag);
        } else if (tag instanceof NBTTagList) {
            NBTTagList tagList = (NBTTagList) tag;
            NBTTagCompound identTag = (NBTTagCompound) tagList.tagAt(0);

            ItemStack[] inventoryStacks = new ItemStack[identTag.getByte("invSize")];

            for (int i = 1; i < tagList.tagCount(); i++) {
                NBTTagCompound itemStackTag = (NBTTagCompound) tagList.tagAt(i);

                byte slot = itemStackTag.getByte("slot");

                if (slot >= 0 && slot < inventoryStacks.length) {
                    inventoryStacks[slot] = ItemStack.loadItemStackFromNBT(itemStackTag);
                }
            }

            return (T) inventoryStacks;
        }

        return null;
    }

    @Override
    public NBTBase writeToTag(String name, Object value) {
        if (value instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) value;

            NBTTagCompound tag = new NBTTagCompound(name);
            itemStack.writeToNBT(tag);

            return tag;
        } else if (value instanceof ItemStack[]) {
            ItemStack[] inventory = (ItemStack[]) value;

            NBTTagList inventoryTag = new NBTTagList(name);

            NBTTagCompound identTag = new NBTTagCompound();
            identTag.setByte("invSize", (byte) inventory.length);
            inventoryTag.appendTag(identTag);

            for (int i = 0; i < inventory.length; i++) {
                if (inventory[i] != null) {
                    NBTTagCompound itemStackTag = new NBTTagCompound();

                    itemStackTag.setByte("slot", (byte) i);
                    inventory[i].writeToNBT(itemStackTag);

                    inventoryTag.appendTag(itemStackTag);
                }
            }

            return inventoryTag;
        }

        return null;
    }
}
