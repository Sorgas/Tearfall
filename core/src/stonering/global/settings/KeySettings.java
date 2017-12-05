package stonering.global.settings;

import com.badlogic.gdx.Input;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alexander on 29.06.2017.
 *
 * Class for loading and storing key binds. InputProcessors should be initialized regarding to this
 */
public class KeySettings {
    private int cameraUp = Input.Keys.NUMPAD_6;
    private int cameraDown = Input.Keys.NUMPAD_3;
    private int cameraSW = Input.Keys.NUMPAD_1;
    private int cameraSE = Input.Keys.NUMPAD_2;
    private int cameraNW = Input.Keys.NUMPAD_4;
    private int cameraNE = Input.Keys.NUMPAD_5;

    public int getCameraUp() {
        return cameraUp;
    }

    public int getCameraDown() {
        return cameraDown;
    }

    public int getCameraSW() {
        return cameraSW;
    }

    public int getCameraSE() {
        return cameraSE;
    }

    public int getCameraNW() {
        return cameraNW;
    }

    public int getCameraNE() {
        return cameraNE;
    }

    public Set<Integer> getKeys() {
        Set<Integer> keys = new HashSet<>();
        keys.add(cameraUp);
        keys.add(cameraDown);
        keys.add(cameraNE);
        keys.add(cameraNW);
        keys.add(cameraSE);
        keys.add(cameraSW);
        return keys;
    }
}
