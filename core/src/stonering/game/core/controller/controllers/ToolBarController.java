package stonering.game.core.controller.controllers;

import stonering.game.core.GameMvc;
import stonering.game.core.view.ui_components.menus.Toolbar;

/**
 * Controller for Toolbar.
 * ParentMenu behavior bases on its state and divided between menus in ParentMenu,
 * so controller simply sends all input events to it.
 *
 * @author Alexander Kuzyakov on 25.12.2017.
 */
public class ToolBarController extends Controller {
    private Toolbar toolbar;

    public ToolBarController(GameMvc gameMvc) {
        super(gameMvc);
    }

    public void init() {
        toolbar = gameMvc.getView().getUiDrawer().getToolbar();
    }

    public boolean handlePress(int keycode) {
        return toolbar.invoke(keycode);
    }
}
