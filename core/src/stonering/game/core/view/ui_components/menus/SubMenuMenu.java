package stonering.game.core.view.ui_components.menus;

import stonering.game.core.GameMvc;

import java.util.HashMap;

/**
 * @author Alexander Kuzyakov
 * created on 15.06.2018
 */
public abstract class SubMenuMenu extends Menu {
    protected HashMap<Character, Menu> menus;

    public SubMenuMenu(GameMvc gameMvc) {
        super(gameMvc);
        menus = new HashMap<>();
    }

    protected void addMenu(Menu menu, String key) {

    }
}
