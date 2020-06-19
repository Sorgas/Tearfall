package stonering.util.logging;

import com.badlogic.gdx.ApplicationLogger;

/**
 * @author Alexander on 31.10.2018.
 */
public class TaggedLogger implements ApplicationLogger {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String DEBUG_TAG = ANSI_CYAN + ":[DEBUG]: " + ANSI_RESET;
    public static final String WARN_TAG = ANSI_YELLOW + ":[WARN]: " + ANSI_RESET;
    public static final String ERROR_TAG = ANSI_RED + ":[ERROR]: " + ANSI_RESET;


    @Override
    public void log(String tag, String message) {
        System.out.println(tag + ": " + message);
    }

    @Override
    public void log(String tag, String message, Throwable exception) {
        System.out.println(tag + ": " + message + exception.getMessage());
    }

    @Override
    public void error(String tag, String message) {
        System.out.println(tag + ERROR_TAG + message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        System.out.println(tag + ERROR_TAG + message + exception.getMessage());
    }

    @Override
    public void debug(String tag, String message) {
        System.out.println(tag + DEBUG_TAG + message);
    }

    public void debugn(String tag, String message) {
        System.out.print(tag + DEBUG_TAG + message);
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        System.out.println(tag + DEBUG_TAG + message + exception.getMessage());
    }

    public void warn(String tag, String message) {
        System.out.println(tag + WARN_TAG + message);
    }

    public void warn(String tag, String message, Throwable exception) {
        System.out.println(tag + WARN_TAG + message + exception.getMessage());
    }
}
