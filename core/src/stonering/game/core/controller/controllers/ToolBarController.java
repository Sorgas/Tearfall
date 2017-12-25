/*
 * Created by Alexander on .
 */

/*
 * Created by Alexander on .
 */

package stonering.game.core.controller.controllers;

import stonering.game.core.model.GameContainer;
import stonering.game.core.view.GameView;
import stonering.game.core.view.Toolbar;

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

    public void pressD() {
        if(toolbar.diggingMenuIsOpen()) {

        } else {
            toolbar.openDiggingMenu();
        }
    }

    public void setDesignationsController(DesignationsController designationsController) {
        this.designationsController = designationsController;
    }
}
