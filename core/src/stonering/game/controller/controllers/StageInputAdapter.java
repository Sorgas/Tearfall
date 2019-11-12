package stonering.game.controller.controllers;

import com.badlogic.gdx.InputAdapter;
import stonering.screen.GameView;

/**
 * Passes input events to top stage in {@link GameView}.
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
        return gameView.getActiveStage().keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return gameView.getActiveStage().touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return gameView.getActiveStage().touchDown(screenX, screenY, pointer, button);
    }
}
