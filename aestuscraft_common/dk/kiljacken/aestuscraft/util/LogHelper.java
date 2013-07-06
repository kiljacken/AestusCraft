package dk.kiljacken.aestuscraft.util;

import java.util.logging.Logger;

import dk.kiljacken.aestuscraft.lib.Reference;

public class LogHelper {
    private static Logger logger;

    public static void init() {
        logger = Logger.getLogger(Reference.MOD_ID);
    }
    
    public static void config(String msg) {
        if (logger == null)
            return;
        
        logger.config(msg);
    }
    
    public static void info(String msg) {
        if (logger == null)
            return;
        
        logger.info(msg);
    }
    
    public static void warning(String msg) {
        if (logger == null)
            return;
        
        logger.warning(msg);
    }
    
    public static void severe(String msg) {
        if (logger == null)
            return;
        
        logger.severe(msg);
    }

}
