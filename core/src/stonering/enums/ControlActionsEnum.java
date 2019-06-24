package stonering.enums;

import com.badlogic.gdx.Input;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum that describes control actions done on ui elements.
 * This actions are mapped to keys in ui widgets.
 * Also stores default mapping.
 *
 * @author Alexander_Kuzyakov on 24.06.2019.
 */
public enum ControlActionsEnum {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    SELECT,
    CANCEL,
    REPEAT;

    static Map<Integer, ControlActionsEnum> mapping = new HashMap<>();

    static {
        mapping.put(Input.Keys.W, UP);
        mapping.put(Input.Keys.S, DOWN);
        mapping.put(Input.Keys.A, LEFT);
        mapping.put(Input.Keys.D, RIGHT);
        mapping.put(Input.Keys.E, SELECT);
        mapping.put(Input.Keys.Q, CANCEL);
        mapping.put(Input.Keys.R, REPEAT);
    }

    public static ControlActionsEnum getAction(int keycode) {
        return mapping.get(keycode);
    }
}
