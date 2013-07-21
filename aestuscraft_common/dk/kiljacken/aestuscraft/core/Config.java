/**
 * AestusCraft
 * 
 * Config.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core;

import java.io.File;

import net.minecraftforge.common.Configuration;
import dk.kiljacken.aestuscraft.AestusCraft;

public class Config {
    public Config(File file) {
        Configuration configuration = new Configuration(file);

        try {
            configuration.load();
        } catch (Exception e) {
            AestusCraft.log.severe("Exception while loading configuration");
            throw new RuntimeException(e);
        } finally {
            configuration.save();
        }
    }
}
