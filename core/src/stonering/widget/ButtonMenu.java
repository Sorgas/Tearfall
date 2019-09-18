package stonering.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.util.global.Logger;
import stonering.util.global.StaticSkin;

import java.util.LinkedHashMap;

/**
 * Table that stores buttons, and maps hotkeys to buttons.
 * Hides itself on Q.
 */
public abstract class ButtonMenu extends Table implements Hideable {
    private LinkedHashMap<Integer, Button> buttons;
    protected boolean forbidEventPass = false; // if true, key events will be handled further

    public ButtonMenu() {
        buttons = new LinkedHashMap<>();
        defaults().right().expandX().fill();
        createDefaultListener();
    }

    /**
     * Presses button with given hotkey. If char is Q and this menu can be hidden, hides itself.
     * Most times there is no need for overriding this for menus. For special closing logic use onHide() method.
     */
    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
//                Logger.UI.logDebug("handling " + keycode + " in button menu");
                event.stop();
                if (buttons.containsKey(keycode)) {
                    Logger.UI.logDebug("Pressing " + Input.Keys.toString(keycode) + " button in " + this);
                    buttons.get(keycode).toggle();
                } else if (keycode == Input.Keys.Q) { //
                    hide();
                    onHide();
                }
                return forbidEventPass;
            }
        });
    }

    /**
     * Creates button with listener and hotkey. Will overwrite buttons with same hotkey.
     */
    protected void createButton(String text, int hotKey, ChangeListener listener, boolean appendHotkey) {
        TextButton button = new TextButton(appendHotkey ? Input.Keys.toString(hotKey) + ": " + text : text, StaticSkin.getSkin());
        button.addListener(listener);
        buttons.put(hotKey, button);
        add(button).row();
    }

    /**
     * Cancels all inputs, like selected tools.
     */
    protected void onHide() {
    }
}
