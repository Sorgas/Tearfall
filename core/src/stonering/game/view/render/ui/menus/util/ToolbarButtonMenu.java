package stonering.game.view.render.ui.menus.util;

import stonering.game.GameMvc;
import stonering.game.view.render.ui.menus.toolbar.Toolbar;

/**
 * Abstract menu for toolbar. Removes itself from {@link Toolbar} on hide.
 * Menus don't have their controllers, all behavior logic is written in their buttons.
 * Keys sets of menus should not overlap.
 *
 *
 * @author Alexander Kuzyakov on 27.12.2017.
 */
public abstract class ToolbarButtonMenu extends ButtonMenu {

    public ToolbarButtonMenu() {
        this.bottom();
    }

    @Override
    public void show() {
        GameMvc.instance().getView().getUiDrawer().getToolbar().addMenu(this);
    }

    @Override
    public void hide() {
        GameMvc.instance().getView().getUiDrawer().getToolbar().hideMenu(this);
    }
}