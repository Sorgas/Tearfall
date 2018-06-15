package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.HashMap;

/**
 * @author Alexander Kuzyakov
 * created on 15.06.2018
 */
public class SubMenuMenu extends Menu {
    protected HashMap<String, Menu> menus;
    protected HashMap<String, TextButton> buttons;

    public SubMenuMenu() {
        super();
        menus = new HashMap<>();
        buttons = new HashMap<>();
    }

    protected void addMenu(Menu menu, String key) {

    }
}
