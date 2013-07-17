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

public interface IAddon {
    public String getParentModId();

    public void loadConfig(Configuration config);

    public void initializeBlocks();

    public void initializeBlockRecipes();

    public void initializeItems();

    public void initializeItemRecipes();

    public void registerTileEntites();

    public void initializeRendering();

    public void postInit();
}
