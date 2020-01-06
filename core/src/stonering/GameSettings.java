package stonering;

import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.util.Properties;

/**
 * Loads gameplay settings. Settings can be changed in menu and saved to file.
 * TODO add difficulties
 *
 * @author Alexander on 12.11.2019.
 */
public enum GameSettings {
    DRAW_ACTION_PROGRESS("draw_action_progress");

    private String VALUE;

    private static Properties instance;

    static {
        instance = new Properties();
        FileHandle file = new FileHandle("resources/init.properties");
        try {
            instance.load(file.read());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    GameSettings(String value) {
        VALUE = value;
    }

    public static String get(GameSettings setting) {
        return instance.getProperty(setting.VALUE);
    }
}
