/**
 * AestusCraft
 * 
 * LocalizationHelper.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.util;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class LocalizationHelper {
    private static final String LOCALIZATION_PATH = "/mods/aec/lang/";
    private static final String[] LANGUAGES = new String[] { "en_US" };

    public static void init() {
        LogHelper.info("Loading localization data");

        for (String language : LANGUAGES) {
            LogHelper.info("Loading locale: " + language);
            LanguageRegistry.instance().loadLocalization(LOCALIZATION_PATH + language + ".xml", language, true);
        }
    }

}
