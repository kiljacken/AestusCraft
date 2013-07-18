/**
 * AestusCraft
 * 
 * Resources.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.lib;

import net.minecraft.util.ResourceLocation;

public class ResourcesLocations {
    public static final String PREFIX = Reference.MOD_ID.toLowerCase();

    // GUIs
    public static final ResourceLocation INSULATED_FURNACE_GUI = new ResourceLocation(PREFIX, "textures/gui/insulated_furnace.png");
    public static final ResourceLocation FUEL_BURNER_GUI = new ResourceLocation(PREFIX, "textures/gui/fuel_burner.png");

    // Textures
    public static final ResourceLocation HEAT_CONDUIT_TEXTURE = new ResourceLocation(PREFIX, "textures/blocks/conduit.png");

    // Models
    public static final String HEAT_CONDUIT_MODEL = "/assets/aec/models/conduit.obj";
}
