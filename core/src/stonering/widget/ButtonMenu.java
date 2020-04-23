package stonering.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import stonering.enums.images.DrawableMap;
import stonering.util.global.Logger;
import stonering.widget.lists.IconTextButton;

import java.util.HashMap;

/**
 * Menu with buttons. 
 * Maps hotkeys to buttons.
 * Toggles buttons when receives hotkey presses. Behavior logic is written in their buttons.
 * Keys sets of same-level menus should not overlap.
 * Hides itself on Q.
 */
public abstract class ButtonMenu extends Table {
    private HashMap<Integer, Button> buttons;
    protected boolean forbidEventPass = false; // if true, key events will be handled further

    public ButtonMenu() {
        buttons = new HashMap<>();
        defaults().right().expandX().fill();
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (buttons.containsKey(keycode)) {
                    Logger.UI.logDebug("Pressing " + Input.Keys.toString(keycode) + " button in " + this);
                    buttons.get(keycode).toggle();
                    return true;
                } else if (keycode == Input.Keys.Q) {

                    return hide();
                }
                return forbidEventPass;
            }
        });
    }

    protected void createButton(String text, int hotKey, ChangeListener listener, boolean appendHotkey) {
        createButton(text, null, hotKey, listener, appendHotkey);
    }

    protected void createButton(String text, String iconName, int hotKey, ChangeListener listener, boolean appendHotkey) {
        Drawable drawable = iconName != null ? DrawableMap.ICON.getDrawable(iconName) : null;
        IconTextButton button = new IconTextButton(drawable, (appendHotkey ? Input.Keys.toString(hotKey) + ": " : "") + text);
        button.addListener(listener);
        buttons.put(hotKey, button);
        add(button).row();
    }

    public abstract boolean hide();
}
