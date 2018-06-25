package stonering.game.core.view.ui_components.menus;

import stonering.game.core.GameMvc;

import java.util.HashMap;

/**
 * @author Alexander Kuzyakov
 * created on 15.06.2018
 */
public abstract class SubMenuMenu extends ButtonMenu {
    protected HashMap<Character, ButtonMenu> menus;

    public SubMenuMenu(GameMvc gameMvc, int menuLevel) {
        super(gameMvc, menuLevel);
        menus = new HashMap<>();
    }

    public void init() {
        super.init();
        menus.values().forEach((menu) -> menu.init());
    }
}
