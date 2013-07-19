/**
 * AestusCraft
 * 
 * JoinedRenderer.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.client.renderer;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

public abstract class JoinedRenderer extends TileEntitySpecialRenderer implements IItemRenderer {
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
                render(null, -0.5F, 0.5F, -0.5F);
                return;
            case EQUIPPED:
                render(null, 0.0F, 0.2F, 0.0F);
                return;
            case EQUIPPED_FIRST_PERSON:
                render(null, 0.0F, 0.4F, 0.0F);
                return;
            case INVENTORY:
                render(null, 0.0F, 0.0F, 0.0F);
                return;
            default:
                return;
        }

    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
        render(tile, x, y, z);
    }

    public abstract void render(TileEntity tile, double x, double y, double z);
}
