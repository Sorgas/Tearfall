package stonering.game.core.view.render.ui.components.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.game.core.GameMvc;
import stonering.utils.global.StaticSkin;

import java.util.HashMap;

/**
 * Abstract menu for toolbar.
 * Holds mappings of hotkeys to buttons and can simulate presses.
 * Menus don't have their controllers, all behavior logic is written in their buttons.
 * Input comes from parent ButtonMenu, through invokeByKey method.
 * Keys sets of menus should not overlap.
 * <p>
 *
 * @author Alexander Kuzyakov on 27.12.2017.
 */
public abstract class ButtonMenu extends Table implements HideableComponent, Invokable {
    protected GameMvc gameMvc;
    protected Toolbar toolbar;
    protected boolean hideable = false;
    protected HashMap<Integer, Button> buttons;

    public ButtonMenu(GameMvc gameMvc, boolean hideable) {
        this.gameMvc = gameMvc;
        this.hideable = hideable;
        buttons = new HashMap<>();
        this.bottom();
    }

    /**
     * For binding sub-components. Should be called from children.
     */
    public void init() {
        this.defaults().right().expandX().fill();
        buttons.values().forEach((button) -> this.add(button).row());
        toolbar = gameMvc.getView().getUiDrawer().getToolbar();
    }

    /**
     * Presses button with given hotkey. By default tries to press button.
     * If char is ESC and this menu can be closed, closes itself.
     * Most times there is no need for overriding this for menus. For special closing logic use reset() method.
     *
     * @param keycode hotkey
     * @return true, if button with given hotkey exists, prevents further handling of this press.
     * False otherwise, handling continues.
     */
    @Override
    public boolean invoke(int keycode) {
        if (buttons.keySet().contains(keycode)) {
            buttons.get(keycode).toggle();
            return true;
        }
        if (keycode == Input.Keys.Q && hideable) {
            hide();
            reset();
            return true;
        }
        return false;
    }

    protected void createButton(String text, int hotKey, ChangeListener listener) {
        TextButton button = new TextButton(text, StaticSkin.getSkin());
        button.addListener(listener);
        buttons.put(hotKey, button);
    }

    /**
     * Cancels all inputs, like selected tools.
     */
    public abstract void reset();

    @Override
    public void show() {
        toolbar.addMenu(this);
    }

    @Override
    public void hide() {
        toolbar.hideMenu(this);
    }
}