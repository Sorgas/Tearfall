package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import stonering.game.core.GameMvc;
import stonering.utils.global.StaticSkin;

import java.util.HashMap;

/**
 * Abstract menu for toolbar.
 * Holds mappings of hotkeys to buttons and can simulate presses.
 * <p>
 * Created by Alexander on 27.12.2017.
 */
public abstract class Menu extends VerticalGroup {
    protected GameMvc gameMvc;
    protected HashMap<Character, Button> buttons;

    public Menu(GameMvc gameMvc) {
        super();
        this.gameMvc = gameMvc;
        buttons = new HashMap<>();
    }

    /**
     * For binding sub-components.
     */
    public abstract void init();

    public boolean invokeByKey(char c) {
        if (buttons.keySet().contains(c)) {
            buttons.get(c).toggle();
            return true;
        }
        return false;
    }

    /**
     * Adds all buttons from map to table. For hiding use clearChildren().
     */
    public void show() {
        buttons.values().forEach(this::addActor);
    }
}