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

import dk.kiljacken.aestuscraft.inventory.ContainerFuelBurner;
import dk.kiljacken.aestuscraft.lib.ResourcesLocations;
import dk.kiljacken.aestuscraft.tileentity.TileFuelBurner;

public class GuiFuelBurner extends GuiContainer {
    private TileFuelBurner m_TileFuelBurner;

    public GuiFuelBurner(InventoryPlayer inventoryPlayer, TileFuelBurner tilefuelBurner) {
        super(new ContainerFuelBurner(inventoryPlayer, tilefuelBurner));

        m_TileFuelBurner = tilefuelBurner;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int scaledMouseX, int scaledMouseY) {
        // Draw insulated furnace name
        String name = m_TileFuelBurner.isInvNameLocalized() ? m_TileFuelBurner.getInvName() : StatCollector.translateToLocal(m_TileFuelBurner.getInvName());
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);

        // Draw inventory label
        String inventoryLabel = StatCollector.translateToLocal("container.inventory");
        fontRenderer.drawString(inventoryLabel, 8, ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float renderPartialTicks, int scaledMouseX, int scaledMouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.renderEngine.func_110577_a(ResourcesLocations.FUEL_BURNER_GUI);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        // Draw heating process
        if (m_TileFuelBurner.getFuelLeft() != 0) {
            int scaledFuelLeft = 16 - m_TileFuelBurner.getFuelLeft() * 16 / m_TileFuelBurner.getFuelHeat();

            drawTexturedModalRect(x + 100, y + 45 + scaledFuelLeft, 176, scaledFuelLeft, 12, 16 - scaledFuelLeft);
        }

        int scaledHeatLevel = Math.round(54 - m_TileFuelBurner.getHeatBuffer() * 54 / TileFuelBurner.MAX_HEAT_BUFFER);
        drawTexturedModalRect(x + 160, y + 16 + scaledHeatLevel, 176, 16 + scaledHeatLevel, 8, 54 - scaledHeatLevel);
    }
}
