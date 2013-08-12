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

import dk.kiljacken.aestuscraft.core.inventory.ContainerFuelBurner;
import dk.kiljacken.aestuscraft.core.tiles.TileFuelBurner;

public class GuiFuelBurner extends GuiContainer {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation("aestuscraft", "textures/gui/fuel_burner.png");
    private TileFuelBurner m_TileFuelBurner;

    public GuiFuelBurner(InventoryPlayer inventoryPlayer, TileFuelBurner tileFuelBurner)
    {
        super(new ContainerFuelBurner(inventoryPlayer, tileFuelBurner));

        m_TileFuelBurner = tileFuelBurner;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int scaledMouseX, int scaledMouseY)
    {
        // Draw insulated furnace name
        String name = m_TileFuelBurner.isInvNameLocalized() ? m_TileFuelBurner.getInvName() : StatCollector.translateToLocal(m_TileFuelBurner.getInvName());
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);

        // Draw inventory label
        String inventoryLabel = StatCollector.translateToLocal("container.inventory");
        fontRenderer.drawString(inventoryLabel, 8, ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float renderPartialTicks, int scaledMouseX, int scaledMouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.renderEngine.func_110577_a(GUI_LOCATION);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        int scaledFuelTicks = 16 - m_TileFuelBurner.getScaledFuelTicksLeft(16);
        if (scaledFuelTicks != 0)
        {
            drawTexturedModalRect(x + 100, y + 45 + scaledFuelTicks, 176, scaledFuelTicks, 12, 16 - scaledFuelTicks);
        }

        int scaledHeatLevel = 54 - m_TileFuelBurner.getScaledHeatLevel(54);
        drawTexturedModalRect(x + 160, y + 16 + scaledHeatLevel, 176, 16 + scaledHeatLevel, 8, 54 - scaledHeatLevel);
    }
}
