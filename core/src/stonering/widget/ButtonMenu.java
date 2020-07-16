package stonering.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import stonering.util.global.StaticSkin;

import java.util.HashMap;

/**
 * Menu with buttons. Maps hotKeys to buttons.
 * Toggles buttons when receives hotKey presses.
 * Adding button with a hotKey replaces previous button hotKey.
 * Hides itself on Q.
 */
public class ButtonMenu extends Table {
    protected HashMap<Integer, Button> buttons;
    protected boolean forbidEventPass = false; // if true, key events will not be handled further
    protected boolean vertical = true; // most menus are verticalssss

    public ButtonMenu() {
        buttons = new HashMap<>();
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (buttons.containsKey(keycode)) {
                    buttons.get(keycode).toggle();
                    return true;
                }
                return forbidEventPass;
            }
        });
    }

    public ButtonMenu(boolean vertical) {
        this();
        this.vertical = vertical;
    }

    public void addButton(String text, int hotKey, Runnable action) {
        registerButton(createButton(Input.Keys.toString(hotKey) + ": " + text, action), hotKey);
    }

    protected Button createButton(String text, Runnable action) {
        TextButton button = new TextButton(text, StaticSkin.skin());
        if (action != null) button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                action.run();
            }
        });
        return button;
    }

    protected void registerButton(Button button, int hotKey) {
        buttons.put(hotKey, button);
        add(button);
        if (vertical) row();
    }
}
