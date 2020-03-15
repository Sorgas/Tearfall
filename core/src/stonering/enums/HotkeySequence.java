package stonering.enums;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * Enumeration of hotkeys to be set to tools buttons. Case with over fetching covered poorly.
 *
 * @author Alexander on 20.10.2018.
 */
public class HotkeySequence {
    private static final int[] hotkeys = {Y, U, I, O, P, H, J, K, L};
    private int index;

    public int getNext() {
        if (index < hotkeys.length) {
            return hotkeys[index++];
        } else {
            return L;
        }
    }
}
