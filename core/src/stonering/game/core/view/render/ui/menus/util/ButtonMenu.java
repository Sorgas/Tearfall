package stonering.game.core.view.render.ui.menus.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.Toolbar;
import stonering.util.global.StaticSkin;
import stonering.util.global.TagLoggersEnum;

import java.util.HashMap;

/**
 * Abstract screen for toolbar.
 * Holds mappings of hotkeys to buttons and can simulate presses.
 * Some buttons show submenus.
 * Menus don't have their controllers, all behavior logic is written in their buttons.
 * Input comes from parent ButtonMenu, through invoke method.
 * Keys sets of menus should not overlap.
 * <p>
 *
 * @author Alexander Kuzyakov on 27.12.2017.
 */
public abstract class ButtonMenu extends Table implements HideableComponent {
    protected GameMvc gameMvc;
    protected Toolbar toolbar;
    protected boolean hideable = false;

    private HashMap<Integer, Button> buttons;

    public ButtonMenu(GameMvc gameMvc, boolean hideable) {
        this.gameMvc = gameMvc;
        this.hideable = hideable;
        buttons = new HashMap<>();
        this.bottom();
    }

    /**
     * Builds screen widget.
     */
    public void init() {
        this.defaults().right().expandX().fill();
        buttons.values().forEach((button) -> this.add(button).row());
        toolbar = gameMvc.getView().getUiDrawer().getToolbar();
        createDefaultListener();
    }

    /**
     * Presses button with given hotkey. By default tries to press button.
     * If char is ESC and this screen can be closed, closes itself.
     * Most times there is no need for overriding this for menus. For special closing logic use reset() method.
     *
     * @return true, if button with given hotkey exists, prevents further handling of this press.
     * False otherwise, handling continues.
     */

    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                TagLoggersEnum.UI.logDebug("handling " + Input.Keys.toString(keycode) + " in ButtonMenu");
                event.stop();
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
        });
    }

    /**
     * Creates button with listener and hotkey.
     *
     * @param text
     * @param hotKey
     * @param listener
     */
    protected void createButton(String text, int hotKey, ChangeListener listener, boolean appendHotkey) {
        TextButton button = new TextButton(appendHotkey ? Input.Keys.toString(hotKey) + ": " + text : text, StaticSkin.getSkin());
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