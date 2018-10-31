package stonering.utils.global;

import com.badlogic.gdx.ApplicationLogger;

import java.util.List;

/**
 * @author Alexander on 31.10.2018.
 */
public class TaggedLogger implements ApplicationLogger {
    private List<String> enabledTags;

    public TaggedLogger(List<String> enabledTags) {
        this.enabledTags = enabledTags;
    }

    @Override
    public void log(String tag, String message) {
        if (enabledTags.contains(tag)) System.out.println(tag + " : " + message);
    }

    @Override
    public void log(String tag, String message, Throwable exception) {
        if (enabledTags.contains(tag)) System.out.println(tag + " : " + message + exception.getMessage());
    }

    @Override
    public void error(String tag, String message) {
        if (enabledTags.contains(tag)) System.out.println(tag + " : error : " + message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        if (enabledTags.contains(tag)) System.out.println(tag + " : error : " + message + exception.getMessage());
    }

    @Override
    public void debug(String tag, String message) {
        if (enabledTags.contains(tag)) System.out.println(tag + " : debug : " + message);

    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        if (enabledTags.contains(tag)) System.out.println(tag + " : debug : " + message + exception.getMessage());
    }
}
