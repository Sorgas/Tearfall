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
public class GameSettings {
    private static Properties instance;

    public static void load() {
        instance = new Properties();
        FileHandle file = new FileHandle("init.properties");
        try {
            instance.load(file.read());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return instance.getProperty(key);
    }
}
