package stonering.game.core.controller.inputProcessors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import stonering.game.core.controller.controllers.DesignationsController;
import stonering.game.core.controller.controllers.GameController;
import stonering.game.core.controller.controllers.ToolBarController;

/**
 * @author Alexander Kuzyakov on 26.12.2017.
 */
public class DesignationsInputProcessor implements InputProcessor {
    private ToolBarController controller;

    public DesignationsInputProcessor(GameController gameController) {
        controller = gameController.getDesignationsController();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ENTER:
                return controller.handleDesignation();
            case Input.Keys.ESCAPE:
                controller.handleCancel();
                return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
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
