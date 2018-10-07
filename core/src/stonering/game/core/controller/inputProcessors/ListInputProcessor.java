package stonering.game.core.controller.inputProcessors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import stonering.game.core.controller.controllers.GameController;
import stonering.game.core.controller.controllers.UIController;
import stonering.game.core.view.render.ui.UIDrawer;

/**
 * Input processor for all lists in game. Sends events to {@link UIDrawer}.
 *
 * @author Alexander on 03.07.2018.
 */
public class ListInputProcessor implements InputProcessor {
    private UIController uiController;

    public ListInputProcessor(GameController controller) {
        uiController = controller.getUiController();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ENTER) {
            return uiController.handleListSelect();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        switch (character) {
            case '[':
                return uiController.handleListDown();
            case ']':
                return uiController.handleListUp();
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
