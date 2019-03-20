package stonering.game.view.render.ui.menus.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.game.GameMvc;
import stonering.util.global.StaticSkin;
import stonering.util.global.TagLoggersEnum;

import java.util.LinkedHashMap;

public abstract class ButtonMenu extends Table implements HideableComponent {
    protected GameMvc gameMvc;
    protected boolean hideable;
    protected boolean defaultHandleResult = false;

    private LinkedHashMap<Integer, Button> buttons;

    public ButtonMenu(boolean hideable) {
        this.gameMvc = GameMvc.getInstance();
        this.hideable = hideable;
        buttons = new LinkedHashMap<>();
    }

    public ButtonMenu(boolean hideable, boolean defaultHandleResult) {
        this(hideable);
        this.defaultHandleResult = defaultHandleResult;
    }

    public ButtonMenu() {
        this(false);
    }

    /**
     * Builds screen widget.
     */
    public void init() {
        this.defaults().right().expandX().fill();
        buttons.values().forEach((button) -> this.add(button).row());
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
                event.stop();
                if (buttons.keySet().contains(keycode)) {
                    TagLoggersEnum.UI.logDebug("handling " + Input.Keys.toString(keycode) + " in " + this);
                    buttons.get(keycode).toggle();
                    return true;
                }
                if (keycode == Input.Keys.Q && hideable) {
                    hide();
                    reset();
                    return true;
                }
                return defaultHandleResult;
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
    }

    /**
     * Cancels all inputs, like selected tools.
     */
    public abstract void reset();
}
