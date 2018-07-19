package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
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
 * @author Alexander Kuzyakov on 27.12.2017.
 */
public abstract class ButtonMenu extends ToolbarComponent {
    protected HashMap<Character, Button> buttons;

    public ButtonMenu(GameMvc gameMvc, boolean hideable) {
        super(gameMvc, hideable);
        buttons = new HashMap<>();
    }

    /**
     * For binding sub-components. Should be called from children.
     */
    public void init() {
        super.init();
        buttons.values().forEach(this::addActor);
    }

    /**
     * Presses button with given hotkey. By default tries to press button.
     * If char is ESC and this menu can be closed, closes itself.
     * Most times there is no need for overriding this for menus. For special closing logic use reset() method.
     *
     * @param c hotkey
     * @return true, if button with given hotkey exists, prevents further handling of this press.
     * False otherwise, handling continues.
     */
    @Override
    public boolean invoke(char c) {
        if (buttons.keySet().contains(c)) {
            buttons.get(c).toggle();
            return true;
        }
        if(c == (char) 27 && hideable) {
            hide();
            reset();
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
     * Cancels all inputs, like selected tools.
     */
    public abstract void reset();
}