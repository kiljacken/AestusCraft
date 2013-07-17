/**
 * AestusCraft
 * 
 * LogHelper.java
 * 
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.util;

import java.util.logging.Logger;

public class LogHelper {
    private static Logger m_Logger;

    public static void init(Logger logger) {
        m_Logger = logger;
    }

    public static void config(String msg) {
        if (m_Logger == null) {
            return;
        }

        m_Logger.config(msg);
    }

    public static void info(String msg) {
        if (m_Logger == null) {
            return;
        }

        m_Logger.info(msg);
    }

    public static void warning(String msg) {
        if (m_Logger == null) {
            return;
        }

        m_Logger.warning(msg);
    }

    public static void severe(String msg) {
        if (m_Logger == null) {
            return;
        }

        m_Logger.severe(msg);
    }
}
