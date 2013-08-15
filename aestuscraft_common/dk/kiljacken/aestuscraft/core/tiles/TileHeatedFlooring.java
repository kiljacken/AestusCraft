package dk.kiljacken.aestuscraft.core.tiles;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import dk.kiljacken.aestuscraft.api.heat.IHeatConsumer;
import dk.kiljacken.aestuscraft.core.network.packets.PacketHeatLevelSync;
import dk.kiljacken.aestuscraft.library.ConnectedTextureHelper;
import dk.kiljacken.aestuscraft.library.SquareSpiral;
import dk.kiljacken.aestuscraft.library.WorkAdaptingDelay;
import dk.kiljacken.aestuscraft.library.WorkAdaptingDelay.WorkAdaptingDelayNBTHandler;
import dk.kiljacken.aestuscraft.library.nbt.NBTUtil.NBTValue;

public class TileHeatedFlooring extends TileHeatConductor implements IHeatConsumer {
    public static float HEAT_PER_TICK = 0.1f;

    @NBTValue(name = "heatLevel")
    private float m_HeatLevel;
    private float m_MaxHeatLevel;

    @NBTValue(name = "delay", handler = WorkAdaptingDelayNBTHandler.class)
    private WorkAdaptingDelay m_Delay;
    private int m_Ticks;

    // TODO Maybe just use CodeChickenCore?
    private SquareSpiral m_Spiral;

    public TileHeatedFlooring()
    {
        super();

        m_HeatLevel = 0;
        m_MaxHeatLevel = 10;
        m_Delay = new WorkAdaptingDelay(100);
        m_Ticks = m_Delay.getDelay();

        m_Spiral = new SquareSpiral(5 * 5);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!worldObj.isRemote)
        {
            boolean hadHeat = getHeatLevel() > HEAT_PER_TICK;
            if (hadHeat)
            {
                setHeatLevel(getHeatLevel() - HEAT_PER_TICK);
            }

            if (--m_Ticks <= 0 && hadHeat)
            {
                int x = xCoord + m_Spiral.getSpiralX();
                int z = zCoord + m_Spiral.getSpiralZ();

                boolean didWork = false;
                for (int yOff = -2; yOff <= 2; yOff++)
                {
                    int y = yCoord + yOff;
                    int blockId = worldObj.getBlockId(x, y, z);
                    int newBlockId = -1;

                    // TODO: Make meltable blocks configurable via IMC
                    if (blockId == Block.snow.blockID)
                    {
                        newBlockId = 0;
                    }
                    else if (blockId == Block.ice.blockID)
                    {
                        newBlockId = Block.waterMoving.blockID;
                    }

                    if (newBlockId != -1)
                    {
                        worldObj.setBlock(x, y, z, newBlockId, 0, 3);
                        didWork = true;
                    }
                }

                m_Spiral.updateSpiral();
                m_Delay.onCycleEnd(didWork);
                m_Ticks = m_Delay.getDelay();
            }
        }
    }

    @Override
    public int getConnectedSides()
    {
        return ConnectedTextureHelper.getConnectedSides(this);
    }

    @Override
    public float getHeatLevel()
    {
        return m_HeatLevel;
    }

    @Override
    public void setHeatLevel(float heatLevel)
    {
        m_HeatLevel = Math.min(heatLevel, getMaxHeatLevel());

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

    @Override
    public float supplyHeat(float amount)
    {
        amount = Math.min(amount, getMaxHeatLevel() - getHeatLevel());

        setHeatLevel(getHeatLevel() + amount);

        return amount;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        // Hacky stuff to work around inheritance of tile renderer
        return AxisAlignedBB.getAABBPool().getAABB(0, Double.POSITIVE_INFINITY, 0, 0, Double.POSITIVE_INFINITY, 0);
    }

    @Override
    public boolean hasMachineFunctionality()
    {
        return true;
    }
}
