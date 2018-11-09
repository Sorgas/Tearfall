package stonering.game.core.controller.controllers;

import stonering.game.core.GameMvc;
import stonering.game.core.view.render.stages.UIDrawer;

/**
 * Handles inputs to UI components.
 * Keeps state of UI components on the screen.
 *
 * @author Alexander on 03.07.2018.
 */
public class UIController extends Controller{
    private UIDrawer uiDrawer;

    public UIController(GameMvc gameMvc) {
        super(gameMvc);
    }

    @Override
    public void init() {
        uiDrawer = gameMvc.getView().getUiDrawer();
    }
}
