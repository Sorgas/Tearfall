package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.utils.global.StaticSkin;

import java.util.HashMap;

/**
 * Abstract menu for toolbar.
 * Holds mappings of hotkeys to buttons and can simulate presses.
 *
 * Created by Alexander on 27.12.2017.
 */
public abstract class Menu extends Table {
    protected HashMap<Character, Button> buttons;

    public Menu() {
        super(StaticSkin.getSkin());
        buttons = new HashMap<>();
    }

    public boolean invokeByKey(char c) {
        if(buttons.keySet().contains(c)) {
            buttons.get(c).toggle();
            return true;
        }
        return false;
    }

    /**
     * Adds all buttons from map to table. For hiding use clearChildren().
     */
    public void show() {
        buttons.values().forEach((button) -> {
            add(button).row();
        });
    }
}