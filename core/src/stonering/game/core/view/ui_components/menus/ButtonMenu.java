package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


/**
 * @author Alexander Kuzyakov
 * created on 15.06.2018
 */
public class ButtonMenu extends Menu {

    public ButtonMenu() {
        super();
    }

    public void addButton(TextButton button, char hotKey) {
        buttons.put(hotKey, button);
    }
}
