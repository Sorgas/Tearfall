package stonering.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import stonering.enums.images.DrawableMap;
import stonering.widget.button.IconTextButton;

import java.util.HashMap;

/**
 * Menu with buttons. Maps hotKeys to buttons.
 * Toggles buttons when receives hotKey presses.
 * Adding button with a hotKey replaces previous button hotKey.
 * Hides itself on Q.
 */
public class ButtonMenu extends Table {
    private HashMap<Integer, Button> buttons;
    protected boolean forbidEventPass = false; // if true, key events will be handled further
    private final int iconSize;

    public ButtonMenu(int iconSize) {
        this.iconSize = iconSize;
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

    public void createButton(String text, int hotKey, Runnable action) {
        createButton(text, null, hotKey, action);
    }

    public void createButton(String text, String iconName, int hotKey, Runnable action) {
        Drawable drawable = iconName != null ? DrawableMap.ICON.getDrawable(iconName) : null;
        
        Input.Keys.toString(hotKey);
        Button button = new IconTextButton(drawable, text, iconSize);
        if (action != null) button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                action.run();
            }
        });
        buttons.put(hotKey, button);
        add(button).row();
    }
}
