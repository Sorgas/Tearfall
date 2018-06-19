package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import stonering.game.core.GameMvc;


/**
 * @author Alexander Kuzyakov
 * created on 15.06.2018
 */
public abstract class ButtonMenu extends Menu {

    public ButtonMenu(GameMvc gameMvc) {
        super(gameMvc);
    }

    public void addButton(TextButton button, char hotKey) {
        buttons.put(hotKey, button);
    }
}
