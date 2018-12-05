package stonering.game.core.controller.controllers;

import com.badlogic.gdx.InputAdapter;
import stonering.game.core.view.GameView;

/**
 * Ensures that input always goes to active stage from view.
 *
 * @author Alexander
 */
public class StageInputAdapter extends InputAdapter {
    private GameView gameView;

    public StageInputAdapter(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public boolean keyDown(int keycode) {
        return gameView.getActiveStage().keyDown(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return gameView.getActiveStage().keyDown(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return gameView.getActiveStage().touchDown(screenX, screenY, pointer, button);
    }
}
