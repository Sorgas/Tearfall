package stonering.enums;

import com.badlogic.gdx.Input;

/**
 * @author Alexander on 20.10.2018.
 */
public class HotkeySequence {
    private static final int[] hotkeys = {Input.Keys.T, Input.Keys.Y, Input.Keys.U, Input.Keys.I, Input.Keys.O, Input.Keys.P,};
    private int index;

    public int getNext() {
        if (index < hotkeys.length) {
            return hotkeys[index++];
        } else {
            return Input.Keys.L;
        }
    }
}
