package stonering.utils.global;

import com.badlogic.gdx.Gdx;

import java.util.List;

/**
 * Enumeration of application-wide loggers which are destinquished by tags and could be enabled or disabled.
 *
 * @author Alexander Kuzyakov
 */
public enum TagLoggersEnum {
    TASKS("task");

    private String tag;
    private String tagWord;
    private boolean enabled;

    /**
     * Updates statuses of loggers. Only loggers specified in tag list will be enabled after this.
     *
     * @param tags list of tags to enable.
     */
    public static void enabletags(List<String> tags) {
        if (tags != null && !tags.isEmpty()) {
            for (TagLoggersEnum logger : TagLoggersEnum.values()) {
                logger.setEnabled(tags.contains(logger.getTag()));
            }
        }
    }

    TagLoggersEnum(String tag) {
        this.tag = tag;
        tagWord = "[" + tag.toUpperCase() + "]";
        enabled = false;
    }

    public String getTag() {
        return tag;
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
        if (enabled) Gdx.app.log(tagWord, message);
    }

    /**
     * Method of a logger. If logger is enabled, logs given message prefixed with 'debug' word and tag of given logger.
     *
     * @param message
     */
    public void logDebug(String message) {
        if (enabled) Gdx.app.debug(tagWord, message);
    }
}
