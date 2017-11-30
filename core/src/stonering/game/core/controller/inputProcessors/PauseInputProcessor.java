package stonering.game.core.controller.inputProcessors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import stonering.game.core.controller.GameController;
import stonering.global.settings.KeySettings;
import stonering.utils.global.NavigationInputBuffer;

/**
 * Created by Alexander on 29.11.2017.
 */
public class PauseInputProcessor implements InputProcessor {
    private KeySettings keySettings;
    private GameController controller;

    public PauseInputProcessor(GameController controller) {
        this.controller = controller;
        keySettings = new KeySettings();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (character == ' ') {
            controller.switchPause();
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}