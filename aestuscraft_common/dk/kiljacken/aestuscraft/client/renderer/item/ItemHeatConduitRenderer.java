/**
 * AestusCraft
 * 
 * ItemHeatConduitRenderer.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.client.renderer.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dk.kiljacken.aestuscraft.client.model.ModelConduit;
import dk.kiljacken.aestuscraft.lib.ResourcesLocations;

@SideOnly(Side.CLIENT)
public class ItemHeatConduitRenderer implements IItemRenderer {
    private ModelConduit m_ModelConduit = new ModelConduit();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case ENTITY:
                renderConduit(-0.5F, 0.5F, 0.5F);
                return;
            case EQUIPPED:
                renderConduit(0.0F, 0.2F, 1.0F);
                return;
            case EQUIPPED_FIRST_PERSON:
                renderConduit(0.0F, 0.4F, 1.0F);
                return;
            case INVENTORY:
                renderConduit(0.0F, 0.0F, 1.0F);
                return;
            default:
                return;
        }
    }

    private void renderConduit(float x, float y, float z) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);

        // Translate
        GL11.glTranslatef(x, y, z);

        // Bind texture
        FMLClientHandler.instance().getClient().renderEngine.func_110577_a(ResourcesLocations.HEAT_CONDUIT_TEXTURE);

        // Render
        m_ModelConduit.render();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
