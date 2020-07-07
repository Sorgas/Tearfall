package stonering.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import stonering.enums.images.DrawableMap;
import stonering.widget.lists.IconTextButton;

import java.util.HashMap;

/**
 * Menu with buttons.
 * Maps hotkeys to buttons.
 * Toggles buttons when receives hotkey presses. Behavior logic is written in their buttons.
 * Keys sets of same-level menus should not overlap.
 * Hides itself on Q.
 */
public abstract class ButtonMenu extends Container<Table> {
    protected Table table;
    private HashMap<Integer, Button> buttons;
    protected boolean forbidEventPass = false; // if true, key events will be handled further

    public ButtonMenu() {
        setActor(table = new Table());
        buttons = new HashMap<>();
        table.defaults().right().growX();
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (buttons.containsKey(keycode)) {
                    buttons.get(keycode).toggle();
                    return true;
                } else if (keycode == Input.Keys.Q) {
                    return hide();
                }
                return forbidEventPass;
            }
        });
    }

    protected void createButton(String text, int hotKey, Runnable action) {
        createButton(text, null, hotKey, action);
    }

    protected void createButton(String text, String iconName, int hotKey, Runnable action) {
        Drawable drawable = iconName != null ? DrawableMap.ICON.getDrawable(iconName) : null;
        IconTextButton button = new IconTextButton(drawable, text);
        if (action != null) button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                action.run();
            }
        });
        buttons.put(hotKey, button);
        table.add(button).row();
    }

    public abstract boolean hide();
}
