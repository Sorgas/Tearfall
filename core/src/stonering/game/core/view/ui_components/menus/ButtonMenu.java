package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.game.core.GameMvc;
import stonering.utils.global.StaticSkin;

import java.util.HashMap;

/**
 * Abstract menu for toolbar.
 * Holds mappings of hotkeys to buttons and can simulate presses.
 * Menus don't have their controllers, all behavior logic is written in them.
 * Input comes from parent ButtonMenu, through invokeByKey method.
 * Keys sets of menus should not overlap.
 * <p>
 * Created by Alexander on 27.12.2017.
 */
public abstract class ButtonMenu extends VerticalGroup {
    protected int menuLevel;
    protected GameMvc gameMvc;
    protected HashMap<Character, Button> buttons;
    protected MenuLevels menuLevels;

    public ButtonMenu(GameMvc gameMvc, int menuLevel) {
        super();
        this.gameMvc = gameMvc;
        this.menuLevel = menuLevel;
        buttons = new HashMap<>();
    }

    /**
     * For binding sub-components. Should be called from children.
     */
    public void init() {
        menuLevels = gameMvc.getView().getUiDrawer().getMenuLevels();
        buttons.values().forEach(this::addActor);
    }

    /**
     * Presses button with given hotkey.
     *
     * @param c hotkey
     * @return true, if button with given hotkey exists, prevents further handling of this press.
     * False otherwise, handling continues.
     */
    public boolean invokeByKey(char c) {
        if(c == (char) 27) {
            hide();
            return true;
        }
        if (buttons.keySet().contains(c)) {
            buttons.get(c).toggle();
            return true;
        }
        return false;
    }

    protected void createButton(String text, char hotKey, ChangeListener listener) {
        TextButton button = new TextButton(text, StaticSkin.getSkin());
        button.addListener(listener);
        buttons.put(hotKey, button);
    }

    /**
     * Adds all buttons from map to table and adds table to MenuLevels widget.
     */
    public void show() {
        menuLevels.addMenu(this, menuLevel);
    }

    /**
     * Removes all buttons.
     */
    public void hide() {
        menuLevels.removeActor(this);
    }
}