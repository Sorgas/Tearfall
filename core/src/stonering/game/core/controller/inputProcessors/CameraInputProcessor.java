package stonering.game.core.controller.inputProcessors;

import com.badlogic.gdx.InputProcessor;
import stonering.game.core.controller.GameController;
import stonering.global.settings.KeySettings;
import stonering.utils.global.NavigationInputBuffer;

/**
 * Created by Alexander on 27.06.2017.
 */
public class CameraInputProcessor implements InputProcessor {
    private KeySettings keySettings;
    private GameController controller;
    private NavigationInputBuffer inputBuffer;

    public CameraInputProcessor(GameController controller, NavigationInputBuffer navigationInputBuffer) {
        this.controller = controller;
        keySettings = new KeySettings();
        inputBuffer = navigationInputBuffer;
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
                    case '1'://Input.Keys.NUMPAD_1:
                        controller.moveCamera(0, 1, 0);
                        return true;
                    case '2'://Input.Keys.NUMPAD_2:
                        controller.moveCamera(1, 0, 0);
                        return true;
                    case '3'://Input.Keys.NUMPAD_3:
                        controller.moveCamera(0, 0, -1);
                        return true;
                    case '4'://Input.Keys.NUMPAD_4:
                        controller.moveCamera(-1, 0, 0);
                        return true;
                    case '5'://Input.Keys.NUMPAD_5:
                controller.moveCamera(0, -1, 0);
                return true;
            case '6'://Input.Keys.NUMPAD_6:
                controller.moveCamera(0, 0, 1);
                return true;
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
