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
    Z_UP,
    Z_DOWN,
    DELETE,
    ADDITIONAL_Z,
    NONE; // to avoid NPE in switches

    static Map<Integer, ControlActionsEnum> mapping = new HashMap<>();

    static {
        mapping.put(Input.Keys.W, UP);
        mapping.put(Input.Keys.S, DOWN);
        mapping.put(Input.Keys.A, LEFT);
        mapping.put(Input.Keys.D, RIGHT);
        mapping.put(Input.Keys.E, SELECT);
        mapping.put(Input.Keys.Q, CANCEL);
        mapping.put(Input.Keys.R, Z_UP);
        mapping.put(Input.Keys.F, Z_DOWN);
        mapping.put(Input.Keys.Z, ADDITIONAL_Z);
        mapping.put(Input.Keys.X, DELETE);
    }

    public static ControlActionsEnum getAction(int keycode) {
        return mapping.getOrDefault(keycode, NONE);
    }
}
