package stonering.game.controller.controllers;

import stonering.game.GameMvc;

/**
 * Abstract controller for handling user input or other events.
 *
 * @author Alexander Kuzyakov on 25.12.2017.
 */
public abstract class Controller {
    protected GameMvc gameMvc;

    public void init() {
        gameMvc = GameMvc.instance();
    }
}
