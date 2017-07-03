package me.Cooltimmetje.CMSBot.Utilities;

import org.slf4j.LoggerFactory;

/**
 * Class for logging stuff to the console
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class Logger {

    /**
     * Logger instance.
     */
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Logger.class);

    /**
     * Logs a info entry to the console.
     *
     * @param string The message that should be logged.
     */
    public static void info(String string){
        log.info(string);
    }

    /**
     * Logs a warning to the console
     *
     * @param string String that explains the warning.
     * @param e Exception for stacktrace.
     */
    public static void warn(String string, Exception e){
        log.warn(string,e);
    }

}
