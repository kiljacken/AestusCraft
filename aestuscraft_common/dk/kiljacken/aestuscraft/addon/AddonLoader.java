/**
 * AestusCraft
 * 
 * Addon.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.addon;

import java.io.IOException;
import java.util.Iterator;

import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import dk.kiljacken.aestuscraft.util.LogHelper;

public class AddonLoader {
    private ClassToInstanceMap<IAddon> m_InstanceMap;

    public AddonLoader() {
        m_InstanceMap = MutableClassToInstanceMap.create();

        try {
            ClassPath classPath = ClassPath.from(getClass().getClassLoader());

            // Loop through all classes in dk.kiljacken.aestuscraft.addon
            for (ClassInfo classInfo : classPath.getTopLevelClassesRecursive(IAddon.class.getPackage().getName())) {
                Class<?> clazz = classInfo.load();

                if (clazz == IAddon.class) {
                    continue;
                }

                if (IAddon.class.isAssignableFrom(clazz)) {
                    try {
                        @SuppressWarnings("unchecked")
                        Class<IAddon> addonClass = (Class<IAddon>) clazz;

                        m_InstanceMap.putInstance(addonClass, addonClass.newInstance());

                        LogHelper.info("Found addon: " + clazz.getSimpleName());
                    } catch (IllegalAccessException | InstantiationException e) {
                        LogHelper.severe("Error while instantiating addon: " + clazz.getSimpleName());
                    }
                }
            }
        } catch (IOException e) {
            LogHelper.severe("Error while instantiating AddonLoader");
        }
    }

    public IAddon getAddonInstance(Class<IAddon> clazz) {
        return m_InstanceMap.getInstance(clazz);
    }

    public void initAll() {
        LogHelper.info("Initializing addons");

        Iterator<IAddon> iter = m_InstanceMap.values().iterator();

        while (iter.hasNext()) {
            IAddon addon = iter.next();

            String parentModId = addon.getParentModId();

            if (parentModId != null) {
                if (!ModLoader.isModLoaded(parentModId)) {
                    iter.remove();
                }
            }
        }
    }

    public void loadAllConfigs(Configuration config) {
        LogHelper.info("Loading addon configurations");

        for (IAddon addon : m_InstanceMap.values()) {
            addon.loadConfig(config);
        }
    }

    public void initializeAllBlocks() {
        LogHelper.info("Initializing addon blocks");

        for (IAddon addon : m_InstanceMap.values()) {
            addon.initializeBlocks();
        }
    }

    public void initializeAllBlockRecipes() {
        LogHelper.info("Initializing addon block recipes");

        for (IAddon addon : m_InstanceMap.values()) {
            addon.initializeBlockRecipes();
        }
    }

    public void initializeAllItems() {
        LogHelper.info("Initializing addon items");

        for (IAddon addon : m_InstanceMap.values()) {
            addon.initializeItems();
        }
    }

    public void initializeAllItemRecipes() {
        LogHelper.info("Initializing addon items recipes");

        for (IAddon addon : m_InstanceMap.values()) {
            addon.initializeItemRecipes();
        }
    }

    public void registerAllTileEntites() {
        LogHelper.info("Registering addon tile entities");

        for (IAddon addon : m_InstanceMap.values()) {
            addon.registerTileEntites();
        }
    }

    public void initializeRenderingAll() {
        LogHelper.info("Initializing addon rendering");

        for (IAddon addon : m_InstanceMap.values()) {
            addon.initializeRendering();
        }
    }

    public void postInitAll() {
        LogHelper.info("Doing addon post initialzation");

        for (IAddon addon : m_InstanceMap.values()) {
            addon.postInit();
        }
    }

    public static AddonLoader instance;
    static {
        instance = new AddonLoader();
    }
}
