/*
 * Created by Alexander on .
 */

/*
 * Created by Alexander on .
 */

package stonering.game.core.controller.controllers;

import stonering.game.core.GameMvc;
import stonering.game.core.model.GameContainer;
import stonering.game.core.view.GameView;

/**
 * Pauses and unpauses game timer. Works with GameContainer directly.
 * Used from Gdx.input and from game events.
 *
 * @author Alexander on 25.12.2017.
 */
public class PauseController extends Controller{
    private GameContainer container;

    public PauseController(GameMvc gameMvc) {
        super(gameMvc);
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

    @Override
    public void init() {
        container = gameMvc.getModel();
    }
}
