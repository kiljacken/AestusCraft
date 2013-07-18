/**
 * AestusCraft
 * 
 * IAddon.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.addon;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.event.FMLInterModComms;

public interface IAddon {
    /**
     * Get the modid of the parent mod
     * 
     * @return The mod id of the addons parent mod, or {@code null} if the addon
     *         doesn't have a parent
     */
    public String getParentModId();

    /**
     * Load addon specific configuration data from the supplied configuration
     * object
     * 
     * @param config
     *            The configuration object to load from
     */
    public void loadConfig(Configuration config);

    /**
     * Initialize addon specific blocks
     */
    public void initializeBlocks();

    /**
     * Initialize recipes for addon specific blocks
     */
    public void initializeBlockRecipes();

    /**
     * Initialize addon specific items
     */
    public void initializeItems();

    /**
     * Initialize recipes for addon specific items
     */
    public void initializeItemRecipes();

    /**
     * Register addon specific tile entities
     */
    public void registerTileEntites();

    /**
     * Initialize addon specific rendering
     */
    public void initializeRendering();

    /**
     * Post initialize addon.
     * 
     * {@link FMLInterModComms} should be done here
     */
    public void postInit();
}
