/**
 * AestusCraft
 * 
 * ConnectedTextureHelper.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.library;

import net.minecraft.tileentity.TileEntity;

public class ConnectedTextureHelper {
    public static int getConnectedSides(TileEntity tile)
    {
        TileEntity[] neighbours = new TileEntity[4];
        neighbours[0] = getNeighbour(tile, 0, 0, -1); // North
        neighbours[1] = getNeighbour(tile, 0, 0, 1); // South
        neighbours[2] = getNeighbour(tile, -1, 0, 0); // West
        neighbours[3] = getNeighbour(tile, 1, 0, 0); // East

        int connectedSides = 0;
        for (int i = 0; i < neighbours.length; i++)
        {
            if (neighbours[i] != null && tile.getClass().isAssignableFrom(neighbours[i].getClass()))
            {
                connectedSides |= 1 << 3 - i;
            }
        }

        return connectedSides;
    }

    private static TileEntity getNeighbour(TileEntity tile, int xOff, int yOff, int zOff)
    {
        return tile.worldObj.getBlockTileEntity(tile.xCoord + xOff, tile.yCoord + yOff, tile.zCoord + zOff);
    }
}
