package stonering.utils.global;

import com.badlogic.gdx.ApplicationLogger;

/**
 * @author Alexander on 31.10.2018.
 */
public class TaggedLogger implements ApplicationLogger {

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
        System.out.println(tag + ":[ERROR]: " + message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        System.out.println(tag + ":[ERROR]: " + message + exception.getMessage());
    }

    @Override
    public void debug(String tag, String message) {
        System.out.println(tag + ":[DEBUG]: " + message);
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        System.out.println(tag + ":[DEBUG]: " + message + exception.getMessage());
    }
}
