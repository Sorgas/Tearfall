/*
 * Created by Alexander on .
 */

package stonering.game.core.controller.controllers;

import stonering.game.core.model.GameContainer;
import stonering.game.core.view.GameView;

/**
 * Abstract controller for handling user input or other events.
 *
 * Created by Alexander on 25.12.2017.
 */
public abstract class Controller {
    protected GameContainer container;
    protected GameView view;

    public Controller(GameContainer container, GameView view) {
        this.container = container;
        this.view = view;
    }
}
