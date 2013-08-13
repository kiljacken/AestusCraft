package dk.kiljacken.aestuscraft.library.nbt.handlers;

import dk.kiljacken.aestuscraft.library.nbt.INBTHandler;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;

public class BooleanNBTHandler implements INBTHandler {
    @SuppressWarnings("unchecked")
    @Override
    public <T> T readFromTag(NBTBase tag)
    {
        if (tag instanceof NBTTagByte)
        {
            return (T) (Object) (((NBTTagByte) tag).data != 0);
        }

        return null;
    }

    @Override
    public NBTBase writeToTag(String name, Object value)
    {
        if (value instanceof Boolean)
        {
            return new NBTTagByte(name, (byte) ((Boolean) value ? 1 : 0));
        }

        return null;
    }
}
