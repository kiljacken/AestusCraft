/**
 * AestusCraft
 * 
 * ForgeDirectionNBTHandler.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.library.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraftforge.common.ForgeDirection;

public class ForgeDirectionNBTHandler implements INBTHandler {
    @SuppressWarnings("unchecked")
    @Override
    public <T> T readFromTag(NBTBase tag) {
        if (tag instanceof NBTTagByte) {
            NBTTagByte direction = (NBTTagByte) tag;

            return (T) ForgeDirection.getOrientation(direction.data);
        }
        return null;
    }

    @Override
    public NBTBase writeToTag(String name, Object value) {
        if (value instanceof ForgeDirection) {
            ForgeDirection direction = (ForgeDirection) value;

            return new NBTTagByte(name, (byte) direction.ordinal());
        }

        return null;
    }
}
