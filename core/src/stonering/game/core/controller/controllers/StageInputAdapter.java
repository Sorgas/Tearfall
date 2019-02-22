package stonering.game.core.controller.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import stonering.game.core.view.GameView;

/**
 * Ensures that input always goes to active stage from {@link GameView}.
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
        int keycode = charToKeycode(character);
        if(keycode < 0 || keycode > 255) return true;
        return gameView.getActiveStage().keyDown(keycode);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return gameView.getActiveStage().touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return gameView.getActiveStage().touchUp(screenX, screenY, pointer, button);
    }

    /**
     * Translates typed character to corresponding keycode.
     * //TODO test letters, numbers, symbols.
     *
     * @param character
     * @return
     */
    private int charToKeycode(char character) {
        return Input.Keys.valueOf(Character.valueOf(character).toString().toUpperCase());
    }
}
