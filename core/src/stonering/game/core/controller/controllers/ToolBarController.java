package stonering.game.core.controller.controllers;

import stonering.game.core.GameMvc;
import stonering.game.core.view.ui_components.menus.Toolbar;

/**
 * Controller for Toolbar. Simply sends input events to it.
 *
 * Created by Alexander on 25.12.2017.
 */
public class ToolBarController extends Controller {
    private Toolbar toolbar;

    public ToolBarController(GameMvc gameMvc) {
        super(gameMvc);
    }

    public void init() {
        toolbar = gameMvc.getView().getUiDrawer().getToolbar();
    }

    public boolean handlePress(char key) {
        return toolbar.handlePress(key);
    }
}
