package stonering;

import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.util.Properties;

/**
 * Loads gameplay settings.
 * TODO add difficulties
 *
 * @author Alexander on 12.11.2019.
 */
public enum GameSettings {
    DRAW_ACTION_PROGRESS("draw_action_progress");

    private String VALUE;

    private static Properties instance;

    GameSettings(String value) {
        VALUE = value;
    }

    public static void load() {
        instance = new Properties();
        FileHandle file = new FileHandle("init.properties");
        try {
            instance.load(file.read());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(GameSettings setting) {
        return instance.getProperty(setting.VALUE);
    }
}
