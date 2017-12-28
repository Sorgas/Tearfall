package stonering.game.core.controller.controllers;

import stonering.game.core.model.GameContainer;
import stonering.game.core.view.GameView;
import stonering.game.core.view.ui_components.Toolbar;

/**
 * Created by Alexander on 25.12.2017.
 *
 * manages state of Toolbar. refers to DesignationsController
 */
public class ToolBarController extends Controller {
    private Toolbar toolbar;
    private DesignationsController designationsController;

    public ToolBarController(GameContainer container, GameView view) {
        super(container, view);
        toolbar = view.getUiDrawer().getToolbar();
    }

    public boolean handlePress(char key) {
        return toolbar.handlePress(key);
    }

    public void setDesignationsController(DesignationsController designationsController) {
        this.designationsController = designationsController;
    }
}
