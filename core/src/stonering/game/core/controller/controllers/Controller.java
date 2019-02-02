package stonering.game.core.controller.controllers;

import stonering.game.core.GameMvc;
import stonering.game.core.model.GameContainer;
import stonering.game.core.view.GameView;

/**
 * Abstract controller for handling user input or other events.
 *
 * @author Alexander Kuzyakov on 25.12.2017.
 */
public abstract class Controller {
    protected GameMvc gameMvc;

    public void init() {
        gameMvc = GameMvc.getInstance();
    }
}
