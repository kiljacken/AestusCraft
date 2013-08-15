package dk.kiljacken.aestuscraft.buildcraft;

import buildcraft.api.core.Position;
import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile.PipeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;
import dk.kiljacken.aestuscraft.core.common.tiles.HeatConsumerBaseTile;
import dk.kiljacken.aestuscraft.library.nbt.NBTUtil.NBTValue;

public class TileEricssonEngine extends HeatConsumerBaseTile implements IPowerEmitter, IPipeConnection {
    public static float HEAT_PER_TICK_PER_STAGE = 1.0f;
    public static float MJ_PER_HEAT = 1.0f;

    private float m_Progress;
    private int m_HeatStage;
    @NBTValue(name = "storedMJ")
    private float m_StoredMJ;

    public TileEricssonEngine()
    {
        super(1600);

        m_Progress = 0;
        m_HeatStage = 0;
        m_StoredMJ = 0.0f;
    }

    @Override
    public void updateEntity()
    {
        float requiredHeat = (m_HeatStage + 1) * HEAT_PER_TICK_PER_STAGE;
        if (getHeatLevel() < requiredHeat)
        {
            if (m_Progress > 0.0f)
            {
                m_Progress = Math.max(m_Progress - getPistonSpeed(), 0.0f);
            }

            return;
        }

        m_Progress += getPistonSpeed();

        if (!worldObj.isRemote)
        {
            setHeatLevel(getHeatLevel() - requiredHeat);
            m_StoredMJ += MJ_PER_HEAT * requiredHeat;
            m_HeatStage = MathHelper.floor_float(getHeatLevel() * 4 / getMaxHeatLevel());
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType().blockID, 0, m_HeatStage);

            if (m_Progress >= 1.0f)
            {
                pushOutEnergy();
            }
        }

        if (m_Progress >= 1.0f)
        {
            m_Progress = 0.0f;
        }
    }

    private void pushOutEnergy()
    {
        ForgeDirection side = getOrientation();
        TileEntity output = worldObj.getBlockTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);

        if (output instanceof IPowerReceptor)
        {
            IPowerReceptor receptor = (IPowerReceptor) output;
            PowerReceiver reciver = receptor.getPowerReceiver(side);

            if (reciver != null)
            {
                float amount = reciver.receiveEnergy(Type.ENGINE, m_StoredMJ, side.getOpposite());
                m_StoredMJ -= amount;
            }
        }
    }

    private float getPistonSpeed()
    {
        return 0.02f * (1 << m_HeatStage);
    }

    public boolean switchOrientation()
    {
        for (int i = getOrientation().ordinal() + 1; i <= getOrientation().ordinal() + 6; ++i)
        {
            ForgeDirection o = ForgeDirection.VALID_DIRECTIONS[i % 6];

            Position pos = new Position(xCoord, yCoord, zCoord, o);
            pos.moveForwards(1);
            TileEntity tile = worldObj.getBlockTileEntity((int) pos.x, (int) pos.y, (int) pos.z);

            if (tile instanceof IPowerReceptor && ((IPowerReceptor) tile).getPowerReceiver(o) != null)
            {
                setOrientation(o);
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));

                return true;
            }
        }
        return false;
    }

    @Override
    public boolean receiveClientEvent(int id, int data)
    {
        if (worldObj.isRemote)
        {
            if (id == 0)
            {
                m_HeatStage = data;
            }
        }

        return true;
    }

    public float getProgress()
    {
        return m_Progress;
    }

    public int getHeatStage()
    {
        return m_HeatStage;
    }

    @Override
    public ConnectOverride overridePipeConnection(PipeType type, ForgeDirection with)
    {
        if (type == PipeType.POWER && with == getOrientation())
        {
            return ConnectOverride.CONNECT;
        }

        return ConnectOverride.DISCONNECT;
    }

    @Override
    public boolean canEmitPowerFrom(ForgeDirection side)
    {
        return side == getOrientation();
    }
}
