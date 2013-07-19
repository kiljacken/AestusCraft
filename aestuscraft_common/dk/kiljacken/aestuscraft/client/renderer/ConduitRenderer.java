/**
 * AestusCraft
 * 
 * ConduitRenderer.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.client.renderer;

import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import dk.kiljacken.aestuscraft.client.model.ModelConduit;
import dk.kiljacken.aestuscraft.lib.ResourcesLocations;
import dk.kiljacken.aestuscraft.tileentity.TileHeatConduit;

public class ConduitRenderer extends JoinedRenderer {
    private ModelConduit m_ModelConduit = new ModelConduit();

    @Override
    public void render(TileEntity tile, double x, double y, double z) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);

        // Translate
        GL11.glTranslated(x, y, z + 1.0);

        // Bind texture
        FMLClientHandler.instance().getClient().renderEngine.func_110577_a(ResourcesLocations.HEAT_CONDUIT_TEXTURE);

        if (tile != null && tile instanceof TileHeatConduit) {
            // Render
            m_ModelConduit.get().renderPart("center");

            int connectedSides = ((TileHeatConduit) tile).getConnectedSides();

            if ((connectedSides & 1) != 0) {
                m_ModelConduit.get().renderPart("connection_down");
            }

            if ((connectedSides & 2) != 0) {
                m_ModelConduit.get().renderPart("connection_up");
            }

            if ((connectedSides & 4) != 0) {
                m_ModelConduit.get().renderPart("connection_north");
            }

            if ((connectedSides & 8) != 0) {
                m_ModelConduit.get().renderPart("connection_south");
            }

            if ((connectedSides & 16) != 0) {
                m_ModelConduit.get().renderPart("connection_west");
            }

            if ((connectedSides & 32) != 0) {
                m_ModelConduit.get().renderPart("connection_east");
            }
        } else {
            m_ModelConduit.render();
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
