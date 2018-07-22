package stonering.game.core.view.ui_components.menus;

import stonering.game.core.GameMvc;

import java.util.HashMap;

/**
 * ButtonMenu with child menus mapped with hotkeys.
 * In oreder to be inited, all possible sub menus should be created in constructor.
 *
 * @author Alexander Kuzyakov on 15.06.2018.
 */
public abstract class SubMenuMenu extends ButtonMenu {
    protected HashMap<Integer, ButtonMenu> menus;

    public SubMenuMenu(GameMvc gameMvc, boolean hideable) {
        super(gameMvc, hideable);
        menus = new HashMap<>();
    }

    public void init() {
        super.init();
        menus.values().forEach(ButtonMenu::init);
    }

    public void showSubMenuByHotkey(int hotkey) {
        menus.get(hotkey).show();
    }
}
