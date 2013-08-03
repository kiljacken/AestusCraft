/**
 * AestusCraft
 * 
 * TileHeatedFlooring.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.tiles;

import net.minecraft.block.Block;
import dk.kiljacken.aestuscraft.core.blocks.BlockInfo;
import dk.kiljacken.aestuscraft.library.ConnectedTextureHelper;
import dk.kiljacken.aestuscraft.library.WorkAdaptingDelay;
import dk.kiljacken.aestuscraft.library.WorkAdaptingDelay.WorkAdaptingDelayNBTHandler;
import dk.kiljacken.aestuscraft.library.nbt.NBTUtil.NBTValue;

// TODO Make heated flooring act like conductors?
public class TileHeatedFlooring extends HeatConsumerBaseTile {
    public static float HEAT_PER_REMOVED_SNOW = 1.0f;

    @NBTValue(name = "connectedSides")
    private int m_ConnectedSides;
    private boolean m_ShouldUpdate;
    @NBTValue(name = "delay", handler = WorkAdaptingDelayNBTHandler.class)
    private WorkAdaptingDelay m_Delay;
    private int m_Ticks;

    // Spiral TODO Maybe just use CodeChickenCore?
    private int m_SpiralX;
    private int m_SpiralZ;
    private int m_SpiralSide;
    private int m_SpiralSideLenth;
    private int m_SpiralISide;
    private int m_SpiralI;
    private int m_SpiralIMax;

    public TileHeatedFlooring() {
        super(400);

        m_ConnectedSides = 0;
        m_ShouldUpdate = true;
        m_Delay = new WorkAdaptingDelay(100);
        m_Ticks = m_Delay.getDelay();

        m_SpiralX = 0;
        m_SpiralZ = 0;
        m_SpiralSide = 0;
        m_SpiralSideLenth = 1;
        m_SpiralISide = 0;
        m_SpiralI = 0;
        m_SpiralIMax = 5 * 5;
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (m_ShouldUpdate) {
                int connetedSides = ConnectedTextureHelper.getConnectedSides(this);

                if (connetedSides != m_ConnectedSides) {
                    m_ConnectedSides = connetedSides;
                    worldObj.addBlockEvent(xCoord, yCoord, zCoord, BlockInfo.BLOCK_HEATED_FLOORING_ID, 0, m_ConnectedSides);
                }

                m_ShouldUpdate = false;
            }

            if (--m_Ticks <= 0) {
                boolean didWork = false;
                int x = xCoord + m_SpiralX;
                int z = zCoord + m_SpiralZ;

                for (int yOff = -2; yOff <= 2; yOff++) {
                    int y = yCoord + yOff;
                    int blockId = worldObj.getBlockId(x, y, z);
                    int newBlockId = 0;

                    if (blockId == Block.snow.blockID) {
                        newBlockId = 0;
                    }

                    if (blockId == Block.ice.blockID) {
                        newBlockId = Block.waterMoving.blockID;
                    }

                    if (newBlockId != 0) {
                        if (getHeatLevel() > HEAT_PER_REMOVED_SNOW) {
                            worldObj.setBlock(x, y, z, newBlockId, 0, 3);
                            setHeatLevel(getHeatLevel() - HEAT_PER_REMOVED_SNOW);
                        }

                        didWork = true;
                    }
                }

                m_SpiralI++;
                m_SpiralISide++;

                switch (m_SpiralSide) {
                    case 0:
                        m_SpiralX++;
                        break;
                    case 1:
                        m_SpiralZ++;
                        break;
                    case 2:
                        m_SpiralX--;
                        break;
                    case 3:
                        m_SpiralZ--;
                        break;
                }

                if (m_SpiralISide == m_SpiralSideLenth) {
                    m_SpiralSide = (m_SpiralSide + 1) % 4;

                    if (m_SpiralSide % 2 == 0) {
                        m_SpiralSideLenth++;
                    }

                    m_SpiralISide = 0;
                }

                if (m_SpiralI >= m_SpiralIMax) {
                    m_SpiralX = 0;
                    m_SpiralZ = 0;
                    m_SpiralSide = 0;
                    m_SpiralSideLenth = 1;
                    m_SpiralISide = 0;
                    m_SpiralI = 0;
                    m_SpiralIMax = 5 * 5;
                }

                m_Delay.onCycleEnd(didWork);
                m_Ticks = m_Delay.getDelay();
            }

        }
    }

    @Override
    public boolean receiveClientEvent(int id, int data) {
        if (worldObj.isRemote) {
            if (id == 0) {
                if (m_ConnectedSides != data) {
                    m_ConnectedSides = data;
                    worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
                }
            }
        }

        return true;
    }

    public int getConnectedSides() {
        return m_ConnectedSides;
    }

    public void setShouldUpdate() {
        m_ShouldUpdate = true;
    }
}
