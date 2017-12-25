package stonering.game.core.controller.inputProcessors;

import com.badlogic.gdx.InputProcessor;
import stonering.game.core.controller.controllers.GameController;
import stonering.game.core.controller.controllers.ToolBarController;
import stonering.global.settings.KeySettings;

/**
 * Created by Alexander on 01.07.2017.
 */
public class ToolBarInputProcessor implements InputProcessor {
    private KeySettings keySettings;
    private GameController controller;
    private ToolBarController toolBarController;

    public ToolBarInputProcessor( GameController controller) {
        this.controller = controller;
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
        switch (character) {
            case 'd':
            case 'D': {
                toolBarController.pressD();
            }
            break;
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
