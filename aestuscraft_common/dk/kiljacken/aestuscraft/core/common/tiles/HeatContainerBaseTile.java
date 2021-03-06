package dk.kiljacken.aestuscraft.core.common.tiles;

import cpw.mods.fml.common.network.PacketDispatcher;
import dk.kiljacken.aestuscraft.api.heat.IHeatContainer;
import dk.kiljacken.aestuscraft.api.heat.IHeatNetwork;
import dk.kiljacken.aestuscraft.core.network.packets.PacketHeatLevelSync;
import dk.kiljacken.aestuscraft.library.nbt.NBTUtil.NBTValue;

public abstract class HeatContainerBaseTile extends BaseTile implements IHeatContainer {
    private IHeatNetwork m_Network;

    @NBTValue(name = "heatLevel")
    private float m_HeatLevel;
    private float m_MaxHeatLevel;

    public HeatContainerBaseTile(float maxHeatLevel)
    {
        m_MaxHeatLevel = maxHeatLevel;
    }

    @Override
    public float getHeatLevel()
    {
        return m_HeatLevel;
    }

    @Override
    public void setHeatLevel(float heatLevel)
    {
        m_HeatLevel = heatLevel;

        // TODO: Limit sync packets sent?
        if (!worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 64, worldObj.provider.dimensionId, PacketHeatLevelSync.from(this).wrap());
        }
    }

    @Override
    public float getMaxHeatLevel()
    {
        return m_MaxHeatLevel;
    }

    public int getScaledHeatLevel(int scale)
    {
        return Math.round(m_HeatLevel * scale / m_MaxHeatLevel);
    }

    @Override
    public IHeatNetwork getNetwork()
    {
        return m_Network;
    }

    @Override
    public void setNetwork(IHeatNetwork network)
    {
        m_Network = network;
    }
}
