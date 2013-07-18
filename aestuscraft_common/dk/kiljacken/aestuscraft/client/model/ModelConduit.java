/**
 * AestusCraft
 * 
 * ModelConduit.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.client.model;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dk.kiljacken.aestuscraft.lib.ResourcesLocations;

@SideOnly(Side.CLIENT)
public class ModelConduit {
    private IModelCustom m_ModelConduit;

    public ModelConduit() {
        m_ModelConduit = AdvancedModelLoader.loadModel(ResourcesLocations.HEAT_CONDUIT_MODEL);
    }

    public void render() {
        m_ModelConduit.renderAll();
    }

    public IModelCustom get() {
        return m_ModelConduit;
    }
}
