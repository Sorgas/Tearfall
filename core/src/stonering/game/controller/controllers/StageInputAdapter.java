package stonering.game.controller.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import stonering.game.view.GameView;

/**
 * Dispatches input to {@link GameView} for invoking UI components.
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
        return gameView.keyDown(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        int keycode = charToKeycode(character);
        if(keycode < 0 || keycode > 255) return true;
        return gameView.keyDown(keycode);
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
