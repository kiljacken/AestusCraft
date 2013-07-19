/**
 * AestusCraft
 * 
 * GuiInsulatedFurnace.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.gui.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import dk.kiljacken.aestuscraft.inventory.ContainerInsulatedFurnace;
import dk.kiljacken.aestuscraft.lib.ResourcesLocations;
import dk.kiljacken.aestuscraft.tileentity.TileInsulatedFurnace;

public class GuiInsulatedFurnace extends GuiContainer {
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

        mc.renderEngine.func_110577_a(ResourcesLocations.INSULATED_FURNACE_GUI);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        // Draw heating progress
        if (m_TileInsulatedFurnace.getHeatingLeft() != 0) {
            int scaledHeatingLeft = m_TileInsulatedFurnace.getHeatingLeft() * 16 / 200;

            for (int i = 0; i < 3; i++) {
                drawTexturedModalRect(x + 63 + i * 18, y + 35 + scaledHeatingLeft, 176, scaledHeatingLeft, 12, 16 - scaledHeatingLeft);
            }
        }

        int scaledHeatLevel = Math.round(54 - m_TileInsulatedFurnace.getHeatLevel() * 54 / m_TileInsulatedFurnace.getMaxHeat());
        drawTexturedModalRect(x + 160, y + 16 + scaledHeatLevel, 176, 16 + scaledHeatLevel, 8, 54 - scaledHeatLevel);
    }
}
