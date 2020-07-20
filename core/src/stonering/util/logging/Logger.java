package stonering.util.logging;


import java.util.List;

/**
 * Enumeration of application-wide loggers which are destinquished by tags and could be enabled or disabled.
 * Name is changed for cleaner syntax on usage.
 * @author Alexander Kuzyakov
 */
public enum Logger {
    TASKS("task"),
    UI("ui"),
    PATH("path"),
    LOADING("loading"),
    GENERAL("general"),
    BUILDING("building"),
    ITEMS("item"),
    GENERATION("generation"),
    ZONES("zones"),
    UNITS("units"),
    CRAFTING("crafting"),
    RENDER("render"),
    INPUT("input"),
    EQUIPMENT("equipment"),
    NEED("need"),
    DESIGNATION("designation"),
    PLANTS("plants");

    private static TaggedLogger logger = new TaggedLogger();

    private String TAG;
    private final String TAG_WORD;
    private boolean enabled;

    Logger(String tag) {
        TAG = tag;
        TAG_WORD = "[" + tag.toUpperCase() + "]";
        enabled = false;
    }

    /**
     * Updates statuses of loggers. Only loggers specified in tag list will be enabled after this.
     *
     * @param tags list of tags to enable.
     */
    public static void enableTags(List<String> tags) {
        if (tags != null && !tags.isEmpty()) {
            for (Logger logger : Logger.values()) {
                logger.setEnabled(tags.contains(logger.TAG));
            }
        }
    }

    /**
     * Enables all loggers.
     */
    public static void enableAll() {
        for (Logger logger : Logger.values()) {
            logger.setEnabled(true);
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Method of a logger. If logger is enabled, logs given message prefixed with tag of given logger.
     *
     * @param message
     */
    public void log(String message) {
        if (enabled) logger.log(TAG_WORD, message);
    }

    /**
     * Method of a logger. If logger is enabled, logs given message prefixed with 'debug' word and tag of given logger.
     *
     * @param message
     */
    public void logDebug(String message) {
        if (enabled) logger.debug(TAG_WORD, message);
    }

    public void logDebugn(String message) {
        if (enabled) logger.debugn(TAG_WORD, message);
    }

    public void logWarn(String message) {
        if (enabled) logger.warn(TAG_WORD, message);
    }

    public void logError(String message) {
        if (enabled) logger.error(TAG_WORD, message);
    }

    public <T> T logError(String message, T value) {
        logError(message);
        return value;
    }

    public <T> T logDebug(String message, T value) {
        logDebug(message);
        return value;
    }

    public <T> T logWarn(String message, T value) {
        logWarn(message);
        return value;
    }
}
