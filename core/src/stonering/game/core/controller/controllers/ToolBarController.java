package stonering.game.core.controller.controllers;

import stonering.game.core.model.GameContainer;
import stonering.game.core.view.GameView;
import stonering.game.core.view.ui_components.Toolbar;

/**
 * Controller for Toolbar. Simply sends input events to it.
 *
 * Created by Alexander on 25.12.2017.
 */
public class ToolBarController extends Controller {
    private Toolbar toolbar;

    public ToolBarController(GameContainer container, GameView view) {
        super(container, view);
        toolbar = view.getUiDrawer().getToolbar();
    }

    public boolean handlePress(char key) {
        return toolbar.handlePress(key);
    }
}
