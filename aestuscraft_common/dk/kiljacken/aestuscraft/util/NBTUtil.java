/**
 * AestusCraft
 * 
 * NBTUtil.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import dk.kiljacken.aestuscraft.lib.StringResources;

public class NBTUtil {
    public static NBTBase createTag(String name, Object value) {
        if (value instanceof Byte) {
            return new NBTTagByte(name, (Byte) value);
        }

        if (value instanceof Short) {
            return new NBTTagShort(name, (Short) value);
        }

        if (value instanceof Integer) {
            return new NBTTagInt(name, (Integer) value);
        }

        if (value instanceof Long) {
            return new NBTTagLong(name, (Long) value);
        }

        if (value instanceof Float) {
            return new NBTTagFloat(name, (Float) value);
        }

        if (value instanceof Double) {
            return new NBTTagDouble(name, (Double) value);
        }

        if (value instanceof String) {
            return new NBTTagString(name, (String) value);
        }

        if (value instanceof byte[]) {
            return new NBTTagByteArray(name, (byte[]) value);
        }

        if (value instanceof int[]) {
            return new NBTTagIntArray(name, (int[]) value);
        }

        if (value instanceof ItemStack) {
            NBTTagCompound tag = new NBTTagCompound(name);

            ((ItemStack) value).writeToNBT(tag);

            return tag;
        }

        if (value instanceof ItemStack[]) {
            return writeInventoryToNBT(name, (ItemStack[]) value);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getTagValue(NBTBase tag) {
        if (tag instanceof NBTTagByte) {
            return (T) (Object) ((NBTTagByte) tag).data;
        }

        if (tag instanceof NBTTagShort) {
            return (T) (Object) ((NBTTagShort) tag).data;
        }

        if (tag instanceof NBTTagInt) {
            return (T) (Object) ((NBTTagInt) tag).data;
        }

        if (tag instanceof NBTTagLong) {
            return (T) (Object) ((NBTTagLong) tag).data;
        }

        if (tag instanceof NBTTagFloat) {
            return (T) (Object) ((NBTTagFloat) tag).data;
        }

        if (tag instanceof NBTTagDouble) {
            return (T) (Object) ((NBTTagDouble) tag).data;
        }

        if (tag instanceof NBTTagString) {
            return (T) ((NBTTagString) tag).data;
        }

        if (tag instanceof NBTTagByteArray) {
            return (T) ((NBTTagByteArray) tag).byteArray;
        }

        if (tag instanceof NBTTagIntArray) {
            return (T) ((NBTTagIntArray) tag).intArray;
        }

        if (tag instanceof NBTTagCompound) {
            NBTTagCompound tagCompound = (NBTTagCompound) tag;

            if (tagCompound.hasKey("id") && tagCompound.hasKey("Count") && tagCompound.hasKey("Damage")) {
                return (T) ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }

        if (tag instanceof NBTTagList) {
            NBTTagList tagList = (NBTTagList) tag;

            if (tagList.tagCount() > 0 && tagList.tagAt(0) instanceof NBTTagCompound) {
                NBTTagCompound tagCompound = (NBTTagCompound) tagList.tagAt(0);

                if (tagCompound.hasKey(StringResources.NBT_TE_INVENTORY_MAGIC) && tagCompound.getByte(StringResources.NBT_TE_INVENTORY_MAGIC) == 0x42) {
                    return (T) readInventoryFromNBT((NBTTagList) tag);
                }
            }
        }

        return null;
    }

    public static NBTTagList writeInventoryToNBT(String name, ItemStack[] inventory) {
        NBTTagList itemStacksTag = new NBTTagList(name);

        NBTTagCompound identTag = new NBTTagCompound();
        identTag.setByte(StringResources.NBT_TE_INVENTORY_MAGIC, (byte) 0x42);
        identTag.setByte(StringResources.NBT_TE_INVENTORY_SIZE, (byte) inventory.length);
        itemStacksTag.appendTag(identTag);

        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                NBTTagCompound itemStackTag = new NBTTagCompound();

                itemStackTag.setByte(StringResources.NBT_TE_INVENTORY_SLOT, (byte) i);
                inventory[i].writeToNBT(itemStackTag);

                itemStacksTag.appendTag(itemStackTag);
            }
        }

        return itemStacksTag;
    }

    public static ItemStack[] readInventoryFromNBT(NBTTagList tag) {
        NBTTagCompound inventorySizeTag = (NBTTagCompound) tag.tagAt(0);
        ItemStack[] inventoryStacks = new ItemStack[inventorySizeTag.getByte(StringResources.NBT_TE_INVENTORY_SIZE)];

        for (int i = 1; i < tag.tagCount(); i++) {
            NBTTagCompound itemStackTag = (NBTTagCompound) tag.tagAt(i);

            byte slot = itemStackTag.getByte(StringResources.NBT_TE_INVENTORY_SLOT);

            if (slot >= 0 && slot < inventoryStacks.length) {
                inventoryStacks[slot] = ItemStack.loadItemStackFromNBT(itemStackTag);
            }
        }

        return inventoryStacks;
    }

    public static void writeStorableToNBT(Object storable, NBTTagCompound nbtTagCompound) {
        Class<?> clazz = storable.getClass();

        do {
            if (!clazz.isAnnotationPresent(NBTStorable.class)) {
                continue;
            }

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(NBTValue.class)) {
                    NBTValue nbtValue = field.getAnnotation(NBTValue.class);

                    boolean isAccesible = field.isAccessible();

                    // If field is not accessible, make it
                    if (!isAccesible) {
                        field.setAccessible(true);
                    }

                    try {
                        Object value = field.get(storable);
                        nbtTagCompound.setTag(nbtValue.name(), createTag(nbtValue.name(), value));
                    } catch (IllegalAccessException e) {
                        LogHelper.severe("Error saving field '" + field.getName() + "' of class '" + storable.getClass().getSimpleName() + "' to nbt");
                    }

                    // Restore accessibility after getting value
                    if (!isAccesible) {
                        field.setAccessible(false);
                    }
                }
            }
        } while ((clazz = clazz.getSuperclass()) != null);
    }

    public static void readStorableFromNBT(Object storable, NBTTagCompound nbtTagCompound) {
        Class<?> clazz = storable.getClass();

        do {
            if (!clazz.isAnnotationPresent(NBTStorable.class)) {
                continue;
            }

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(NBTValue.class)) {
                    NBTValue nbtValue = field.getAnnotation(NBTValue.class);

                    boolean isAccesible = field.isAccessible();

                    // If field is not accessible, make it
                    if (!isAccesible) {
                        field.setAccessible(true);
                    }

                    try {
                        Object value = getTagValue(nbtTagCompound.getTag(nbtValue.name()));
                        field.set(storable, value);
                    } catch (IllegalAccessException e) {
                        LogHelper.severe("Error loading field '" + field.getName() + "' of class '" + storable.getClass().getSimpleName() + "' from nbt");
                    }

                    // Restore accessibility after setting value
                    if (!isAccesible) {
                        field.setAccessible(false);
                    }
                }
            }
        } while ((clazz = clazz.getSuperclass()) != null);
    }

    /**
     * Annotation identifying an object as storable in nbt data
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface NBTStorable {
    }

    /**
     * Annotation identifying fields that should be stored in the objects nbt
     * data
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface NBTValue {
        String name();
    }
}
