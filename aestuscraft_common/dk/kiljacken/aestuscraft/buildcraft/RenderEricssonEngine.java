package dk.kiljacken.aestuscraft.buildcraft;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.UP;
import static net.minecraftforge.common.ForgeDirection.WEST;

import org.lwjgl.opengl.GL11;

import dk.kiljacken.aestuscraft.core.client.rendering.TileRenderer;
import dk.kiljacken.aestuscraft.core.common.tiles.BaseTile;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;

public class RenderEricssonEngine extends TileRenderer {
    private static final ResourceLocation CHAMBER_TEXTURE = new ResourceLocation("aestuscraft:textures/blocks/buildcraft/chamber.png");
    private static final ResourceLocation[] TRUNKS = new ResourceLocation[] { new ResourceLocation("aestuscraft:textures/blocks/buildcraft/trunk_blue.png"),
            new ResourceLocation("aestuscraft:textures/blocks/buildcraft/trunk_green.png"),
            new ResourceLocation("aestuscraft:textures/blocks/buildcraft/trunk_yellow.png"),
            new ResourceLocation("aestuscraft:textures/blocks/buildcraft/trunk_red.png") };
    private static final ResourceLocation BASE_TEXTURE = new ResourceLocation("aestuscraft:textures/blocks/buildcraft/engine_ericsson.png");
    private static final float SCALE = 1.0f / 16.0f;
    private static final float CHAMBER_SIZE = 2.0f / 16.0f;
    private static final float[] ANGLE_MAP = new float[6];
    static
    {
        ANGLE_MAP[EAST.ordinal()] = (float) -Math.PI / 2;
        ANGLE_MAP[WEST.ordinal()] = (float) Math.PI / 2;
        ANGLE_MAP[UP.ordinal()] = 0;
        ANGLE_MAP[DOWN.ordinal()] = (float) Math.PI;
        ANGLE_MAP[SOUTH.ordinal()] = (float) Math.PI / 2;
        ANGLE_MAP[NORTH.ordinal()] = (float) -Math.PI / 2;
    }

    private ModelBase model = new ModelBase() {
    };
    private ModelRenderer box;
    private ModelRenderer trunk;
    private ModelRenderer movingBox;
    private ModelRenderer chamber;

    public RenderEricssonEngine()
    {
        setTileEntityRenderer(TileEntityRenderer.instance);

        box = new ModelRenderer(model, 0, 1);
        box.addBox(-8.0f, -8.0f, -8.0f, 16, 4, 16);
        box.rotationPointX = 8.0f;
        box.rotationPointY = 8.0f;
        box.rotationPointZ = 8.0f;

        trunk = new ModelRenderer(model, 1, 1);
        trunk.addBox(-4.0f, -4.0f, -4.0f, 8, 12, 8);
        trunk.rotationPointX = 8.0f;
        trunk.rotationPointY = 8.0f;
        trunk.rotationPointZ = 8.0f;

        movingBox = new ModelRenderer(model, 0, 1);
        movingBox.addBox(-8.0f, -4.0f, -8.0f, 16, 4, 16);
        movingBox.rotationPointX = 8.0f;
        movingBox.rotationPointY = 8.0f;
        movingBox.rotationPointZ = 8.0f;

        chamber = new ModelRenderer(model, 1, 1);
        chamber.addBox(-5.0f, -4.0f, -5.0f, 10, 2, 10);
        chamber.rotationPointX = 8.0f;
        chamber.rotationPointY = 8.0f;
        chamber.rotationPointZ = 8.0f;
    }

    @Override
    public void render(BaseTile tile, double x, double y, double z)
    {
        float progress = 0.25f;
        int heatStage = 0;
        ForgeDirection orientation = ForgeDirection.UP;

        if (tile instanceof TileEricssonEngine)
        {
            TileEricssonEngine engine = (TileEricssonEngine) tile;

            progress = engine.getProgress();
            heatStage = engine.getHeatStage();
            orientation = engine.getOrientation();
        }

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glColor3f(1, 1, 1);

        GL11.glTranslatef((float) x, (float) y, (float) z);

        float step;
        if (progress > 0.5)
        {
            step = 7.99F - (progress - 0.5F) * 2F * 7.99F;
        }
        else
        {
            step = progress * 2F * 7.99F;
        }

        float[] angle = { 0, 0, 0 };
        float[] translate = { orientation.offsetX, orientation.offsetY, orientation.offsetZ };

        switch (orientation)
        {
        case EAST:
        case WEST:
        case DOWN:
            angle[2] = ANGLE_MAP[orientation.ordinal()];
            break;
        case SOUTH:
        case NORTH:
        default:
            angle[0] = ANGLE_MAP[orientation.ordinal()];
            break;
        }

        for (Object box : model.boxList)
        {
            ModelRenderer boxRenderer = (ModelRenderer) box;
            boxRenderer.rotateAngleX = angle[0];
            boxRenderer.rotateAngleY = angle[1];
            boxRenderer.rotateAngleZ = angle[2];
        }

        func_110628_a(BASE_TEXTURE);
        box.render(SCALE);

        float translatefact = step / 16;
        GL11.glTranslatef(translate[0] * translatefact, translate[1] * translatefact, translate[2] * translatefact);
        movingBox.render(SCALE);
        GL11.glTranslatef(-translate[0] * translatefact, -translate[1] * translatefact, -translate[2] * translatefact);

        func_110628_a(CHAMBER_TEXTURE);
        for (int i = 0; i <= step + 2; i += 2)
        {
            chamber.render(SCALE);
            GL11.glTranslatef(translate[0] * CHAMBER_SIZE, translate[1] * CHAMBER_SIZE, translate[2] * CHAMBER_SIZE);
        }

        for (int i = 0; i <= step + 2; i += 2)
        {
            GL11.glTranslatef(-translate[0] * CHAMBER_SIZE, -translate[1] * CHAMBER_SIZE, -translate[2] * CHAMBER_SIZE);
        }

        func_110628_a(TRUNKS[heatStage]);
        trunk.render(SCALE);

        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
