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
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.Configuration;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.google.common.reflect.Reflection;

import cpw.mods.fml.common.Loader;
import dk.kiljacken.aestuscraft.util.LogHelper;

public class AddonLoader {
    private static List<Class<IAddon>> m_Addons;
    private static ClassToInstanceMap<IAddon> m_InstanceMap;

    @SuppressWarnings("unchecked")
    public static void init() {
        m_Addons = new ArrayList<>();

        try {
            ClassPath classPath = ClassPath.from(AddonLoader.class.getClassLoader());

            // Loop through all classes in dk.kiljacken.aestuscraft.addon
            for (ClassInfo classInfo : classPath.getTopLevelClassesRecursive(Reflection.getPackageName(AddonLoader.class))) {
                Class<?> clazz = classInfo.load();

                if (clazz != IAddon.class && IAddon.class.isAssignableFrom(clazz)) {
                    LogHelper.info("Found addon: " + clazz.getSimpleName());

                    m_Addons.add((Class<IAddon>) clazz);
                }
            }
        } catch (IOException e) {
            LogHelper.severe("Error while instantiating AddonLoader");
        }
    }

    public static <T extends IAddon> T getAddonInstance(Class<T> clazz) {
        return m_InstanceMap.getInstance(clazz);
    }

    public static void initializeAddons() {
        LogHelper.info("Initializing addons");

        ImmutableClassToInstanceMap.Builder<IAddon> builder = ImmutableClassToInstanceMap.builder();

        for (Class<IAddon> addonClass : m_Addons) {
            try {
                IAddon addon = addonClass.newInstance();

                if (Loader.isModLoaded(addon.getParentModId())) {
                    builder.put(addonClass, addon);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                LogHelper.severe("Error while instantiating addon: " + addonClass.getSimpleName());
            }
        }

        m_InstanceMap = builder.build();
    }

    public static void loadAllConfigs(Configuration config) {
        LogHelper.info("Loading addon configurations");

        for (IAddon addon : m_InstanceMap.values()) {
            addon.loadConfig(config);
        }
    }

    public static void initializeAllBlocks() {
        LogHelper.info("Initializing addon blocks");

        for (IAddon addon : m_InstanceMap.values()) {
            addon.initializeBlocks();
        }
    }

    public static void initializeAllBlockRecipes() {
        LogHelper.info("Initializing addon block recipes");

        for (IAddon addon : m_InstanceMap.values()) {
            addon.initializeBlockRecipes();
        }
    }

    public static void initializeAllItems() {
        LogHelper.info("Initializing addon items");

        for (IAddon addon : m_InstanceMap.values()) {
            addon.initializeItems();
        }
    }

    public static void initializeAllItemRecipes() {
        LogHelper.info("Initializing addon items recipes");

        for (IAddon addon : m_InstanceMap.values()) {
            addon.initializeItemRecipes();
        }
    }

    public static void registerAllTileEntites() {
        LogHelper.info("Registering addon tile entities");

        for (IAddon addon : m_InstanceMap.values()) {
            addon.registerTileEntites();
        }
    }

    public static void initializeRenderingAll() {
        LogHelper.info("Initializing addon rendering");

        for (IAddon addon : m_InstanceMap.values()) {
            addon.initializeRendering();
        }
    }

    public static void postInitAll() {
        LogHelper.info("Doing addon post initialization");

        for (IAddon addon : m_InstanceMap.values()) {
            addon.postInit();
        }
    }
}
