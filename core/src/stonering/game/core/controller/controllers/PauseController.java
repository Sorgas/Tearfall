/*
 * Created by Alexander on .
 */

/*
 * Created by Alexander on .
 */

package stonering.game.core.controller.controllers;

import stonering.game.core.model.GameContainer;
import stonering.game.core.view.GameView;

/**
 * Created by Alexander on 25.12.2017.
 *
 * works with GameContainer directly
 */
public class PauseController extends Controller{

    public PauseController(GameContainer container, GameView view) {
        super(container, view);
    }

    public void switchPause() {
        container.pauseGame();
    }

    public void pause() {
        container.pauseGame(true);
    }

    public void unpause() {
        container.pauseGame(false);
    }
}
