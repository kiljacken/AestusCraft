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
import dk.kiljacken.aestuscraft.tileentity.TileInsulatedFurnace;

public class GuiInsulatedFurnace extends GuiContainer {
    private TileInsulatedFurnace m_TileEntityInsulatedFurnace;

    public GuiInsulatedFurnace(InventoryPlayer inventoryPlayer, TileInsulatedFurnace tileEntityInsulatedFurnace) {
        super(new ContainerInsulatedFurnace(inventoryPlayer, tileEntityInsulatedFurnace));

        m_TileEntityInsulatedFurnace = tileEntityInsulatedFurnace;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int scaledMouseX, int scaledMouseY) {
        String s = m_TileEntityInsulatedFurnace.isInvNameLocalized() ? m_TileEntityInsulatedFurnace.getInvName() : StatCollector.translateToLocal(m_TileEntityInsulatedFurnace.getInvName());
        fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 0x404040);

        String inventoryLabel = StatCollector.translateToLocal("container.inventory");
        fontRenderer.drawString(inventoryLabel, 8, ySize - 96 + 2, 0x404040);

        String heatLevel = String.valueOf(m_TileEntityInsulatedFurnace.getHeatLevel());
        fontRenderer.drawString(" - Heat Level: " + heatLevel, 8 + fontRenderer.getStringWidth(inventoryLabel), ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float renderPartialTicks, int scaledMouseX, int scaledMouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.renderEngine.bindTexture("/gui/furnace.png");

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        if (m_TileEntityInsulatedFurnace.getFuelLeft() > 0 && m_TileEntityInsulatedFurnace.getFuelHeat() > 0) {
            int scaledFuelLeft = m_TileEntityInsulatedFurnace.getFuelLeft() * 12 / m_TileEntityInsulatedFurnace.getFuelHeat();

            drawTexturedModalRect(x + 56, y + 36 + 12 - scaledFuelLeft, 176, 12 - scaledFuelLeft, 14, scaledFuelLeft + 2);
        }

        if (m_TileEntityInsulatedFurnace.getHeatingLeft() != 0) {
            int scaledHeatingLeft = 24 - m_TileEntityInsulatedFurnace.getHeatingLeft() * 24 / 200;

            drawTexturedModalRect(x + 79, y + 34, 176, 14, scaledHeatingLeft + 1, 16);
        }
    }
}
