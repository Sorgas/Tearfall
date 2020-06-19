package stonering;

import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import stonering.util.logging.Logger;

/**
 * Loads game settings. Settings can be changed in menu and saved to file.
 * If file or some properties are missing, default values are used.
 * TODO add difficulties
 *
 * @author Alexander on 12.11.2019.
 */
public enum GameSettings {
    DRAW_ACTION_PROGRESS("draw_action_progress", 1),
    UI_SCALE("ui_scale", 1),
    CLOSE_TOOLBAR_ON_TOOL_CANCEL("close_toolbar_on_tool_cancel", 0);

    private final String PROPERTY_NAME;
    private final float DEFAULT_VALUE; // set in enum
    public float VALUE; // red from file or set to default

    private static Properties instance;
    private static final String CONFIG_PATH = "resources/init.properties";

    static {
        instance = new Properties();
        FileHandle file = new FileHandle(CONFIG_PATH);
        if (file.exists()) {
            try {
                instance.load(file.read());
                Arrays.stream(GameSettings.values()).forEach(value -> {
                    String propertyValue = instance.getProperty(value.PROPERTY_NAME);
                    try {
                        value.VALUE = Float.parseFloat(propertyValue);
                    } catch (NumberFormatException | NullPointerException e) {
                        Logger.LOADING.logWarn(value.PROPERTY_NAME + " not found in " + CONFIG_PATH + ". Default value " + value.DEFAULT_VALUE + " is used");
                        value.VALUE = value.DEFAULT_VALUE;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                setDefaultValues();
            }
        } else {
            Logger.LOADING.logWarn(CONFIG_PATH + " cannot be loaded. Default values for setting will be applied");
            setDefaultValues();
        }
    }

    private static void setDefaultValues() {
        Arrays.stream(GameSettings.values())
                .forEach(value -> value.VALUE = value.DEFAULT_VALUE);
    }

    public static void persist() {
        FileHandle file = new FileHandle(CONFIG_PATH);
        if (!file.exists()) {
            Logger.LOADING.logWarn(CONFIG_PATH + " cannot be written. Settings will not be saved");
        } else {
            try {
                instance.store(file.write(false), "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    GameSettings(String name, int defaultValue) {
        PROPERTY_NAME = name;
        DEFAULT_VALUE = defaultValue;
    }

    public void set(String value) {
        instance.setProperty(PROPERTY_NAME, value);
    }
}
