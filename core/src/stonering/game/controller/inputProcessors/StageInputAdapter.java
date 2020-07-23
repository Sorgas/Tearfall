package stonering.game.controller.inputProcessors;

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
        return gameView.getActiveStage().touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyUp(int keycode) {
        return gameView.getActiveStage().keyUp(keycode);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return gameView.getActiveStage().touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return gameView.getActiveStage().mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return gameView.getActiveStage().scrolled(amount);
    }
}
