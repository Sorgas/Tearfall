package stonering.game.core.view.render.ui.components.menus.util;

import stonering.game.core.GameMvc;

import java.util.HashMap;

/**
 * ButtonMenu with {@link MenuItem} mapped with hotkeys.
 * In oreder to be inited, all possible sub menus should be created in constructor.
 *
 * @author Alexander Kuzyakov on 15.06.2018.
 */
public abstract class ItemMenu extends NavigableMenu {
    protected HashMap<Integer, MenuItem> items; //hotkeys to items

    public ItemMenu(GameMvc gameMvc, boolean hideable) {
        super(gameMvc, hideable);
        items = new HashMap<>();
    }

    public void init() {
        super.init();
        items.values().forEach(menuItem -> init());
    }

    public void showSubMenuByHotkey(int hotkey) {
        items.get(hotkey).call();
    }
}
