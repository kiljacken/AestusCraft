package dk.kiljacken.aestuscraft.library;

import dk.kiljacken.aestuscraft.library.nbt.INBTHandler;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

public class WorkAdaptingDelay {
    private int m_MaxDelay;
    private int m_Delay;

    public WorkAdaptingDelay(int maxDelay)
    {
        this.m_MaxDelay = maxDelay;
        this.m_Delay = 0;
    }

    public void onCycleEnd(boolean didWork)
    {
        int deltaDelay = didWork ? -m_Delay / 2 : m_Delay;

        m_Delay = MathHelper.clamp_int(m_Delay + deltaDelay, 0, m_MaxDelay);
    }

    public int getDelay()
    {
        return m_Delay;
    }

    public static class WorkAdaptingDelayNBTHandler implements INBTHandler {
        @SuppressWarnings("unchecked")
        @Override
        public <T> T readFromTag(NBTBase tag)
        {
            if (tag instanceof NBTTagCompound)
            {
                NBTTagCompound compound = (NBTTagCompound) tag;

                WorkAdaptingDelay delay = new WorkAdaptingDelay(compound.getInteger("maxDelay"));
                delay.m_Delay = compound.getInteger("delay");

                return (T) delay;
            }

            return null;
        }

        @Override
        public NBTBase writeToTag(String name, Object value)
        {
            if (value instanceof WorkAdaptingDelay)
            {
                WorkAdaptingDelay delay = (WorkAdaptingDelay) value;

                NBTTagCompound tag = new NBTTagCompound(name);
                tag.setInteger("maxDelay", delay.m_MaxDelay);
                tag.setInteger("delay", delay.m_Delay);

                return tag;
            }

            return null;
        }
    }
}
