/**
 * AestusCraft
 * 
 * TileRenderer.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.client.rendering;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import dk.kiljacken.aestuscraft.core.blocks.tiles.BaseTile;

public abstract class TileRenderer extends TileEntitySpecialRenderer implements IItemRenderer {
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
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        render((BaseTile) tileEntity, x, y, z);
    }

    /**
     * Renders the tile
     * 
     * @param tile The tile instance we're rendering. {@code null} if rendering as item
     * @param x Coordinate on the X-axis to render at
     * @param y Coordinate on the Y-axis to render at
     * @param z Coordinate on the Z-axis to render at
     */
    public abstract void render(BaseTile tile, double x, double y, double z);
}
