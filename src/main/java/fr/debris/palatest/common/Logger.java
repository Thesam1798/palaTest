/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 15/05/2021 : 20:21
 */
package fr.debris.palatest.common;

import cpw.mods.fml.common.FMLLog;

/**
 * Class pour print des log simplement
 */
public class Logger {

    private static final org.apache.logging.log4j.Logger log = FMLLog.getLogger();

    // SonarLint : java:S1118 : Utility classes should not have public constructors
    private Logger() {
        throw new IllegalStateException("Utility class");
    }

    public static void log(String message) {
        log.info(String.format("[%s] [%s] > %s", Reference.MOD_NAME, Reference.MOD_VERSION, message));
    }

    public static void error(String message) {
        log.error(String.format("[%s] [%s] > Error Message : ", Reference.MOD_NAME, Reference.MOD_VERSION));
        log.error(message);
    }
}
