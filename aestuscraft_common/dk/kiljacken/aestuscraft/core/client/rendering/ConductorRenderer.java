package dk.kiljacken.aestuscraft.core.client.rendering;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import dk.kiljacken.aestuscraft.core.common.tiles.BaseTile;
import dk.kiljacken.aestuscraft.core.tiles.TileHeatConductor;

public class ConductorRenderer extends TileRenderer {
    private IModelCustom m_Model = AdvancedModelLoader.loadModel("/assets/aestuscraft/models/conductor.obj");
    private ResourceLocation m_Texture = new ResourceLocation("aestuscraft", "textures/models/conductor.png");

    @Override
    public void render(BaseTile tile, double x, double y, double z)
    {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glTranslatef((float) x, (float) y, (float) z + 1);

        FMLClientHandler.instance().getClient().renderEngine.func_110577_a(m_Texture);

        int connectedSides = 0b111111;
        if (tile instanceof TileHeatConductor)
        {
            connectedSides = ((TileHeatConductor) tile).getConnectedSides();
        }

        m_Model.renderPart("center");

        if ((connectedSides & 1) != 0)
        {
            m_Model.renderPart("connection_down");
        }

        if ((connectedSides & 2) != 0)
        {
            m_Model.renderPart("connection_up");
        }

        if ((connectedSides & 4) != 0)
        {
            m_Model.renderPart("connection_north");
        }

        if ((connectedSides & 8) != 0)
        {
            m_Model.renderPart("connection_south");
        }

        if ((connectedSides & 16) != 0)
        {
            m_Model.renderPart("connection_west");
        }

        if ((connectedSides & 32) != 0)
        {
            m_Model.renderPart("connection_east");
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
