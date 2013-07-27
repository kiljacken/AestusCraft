/**
 * AestusCraft
 * 
 * GuiFuelBurner.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import dk.kiljacken.aestuscraft.core.inventory.ContainerInsulatedFurnace;
import dk.kiljacken.aestuscraft.core.tiles.TileInsulatedFurnace;

public class GuiInsulatedFurnace extends GuiContainer {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation("aestuscraft", "textures/gui/insulated_furnace.png");
    private TileInsulatedFurnace m_TileInsulatedFurnace;

    public GuiInsulatedFurnace(InventoryPlayer inventoryPlayer, TileInsulatedFurnace tileInsulatedFurnace) {
        super(new ContainerInsulatedFurnace(inventoryPlayer, tileInsulatedFurnace));

        m_TileInsulatedFurnace = tileInsulatedFurnace;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int scaledMouseX, int scaledMouseY) {
        // Draw insulated furnace name
        String name = m_TileInsulatedFurnace.isInvNameLocalized() ? m_TileInsulatedFurnace.getInvName() : StatCollector.translateToLocal(m_TileInsulatedFurnace.getInvName());
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);

        // Draw inventory label
        String inventoryLabel = StatCollector.translateToLocal("container.inventory");
        fontRenderer.drawString(inventoryLabel, 8, ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float renderPartialTicks, int scaledMouseX, int scaledMouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.renderEngine.func_110577_a(GUI_LOCATION);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        int scaledBurnTicks = 16 - m_TileInsulatedFurnace.getScaledBurnTicksLeft(16);
        for (int i = 0; i < 3; i++) {
            drawTexturedModalRect(x + 63 + i * 18, y + 35 + scaledBurnTicks, 176, scaledBurnTicks, 12, 16 - scaledBurnTicks);
        }

        int scaledHeatLevel = 54 - m_TileInsulatedFurnace.getScaledHeatLevel(54);
        drawTexturedModalRect(x + 160, y + 16 + scaledHeatLevel, 176, 16 + scaledHeatLevel, 8, 54 - scaledHeatLevel);
    }
}
