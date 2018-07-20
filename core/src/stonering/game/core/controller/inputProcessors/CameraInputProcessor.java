package stonering.game.core.controller.inputProcessors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import stonering.game.core.controller.controllers.CameraConroller;
import stonering.game.core.controller.controllers.GameController;
import stonering.global.settings.KeySettings;

/**
 * Input processor for camera.
 * Keys set should not intersect with others.
 *
 * @author Alexander Kuzyakov on 27.06.2017.
 */
public class CameraInputProcessor implements InputProcessor {
    private KeySettings keySettings;
    private CameraConroller controller;

    public CameraInputProcessor(GameController controller) {
        this.controller = controller.getCameraConroller();
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
        switch (character) {
            case 'r':
                controller.moveCamera(0, 0, -1);
                return true;
            case 'f':
                controller.moveCamera(0, 0, 1);
                return true;
            case 'w':
            case 'a':
            case 's':
            case 'd':
            case 'W':
            case 'A':
            case 'S':
            case 'D':
                tryMoveLR();
                return true;
        }
        return false;
    }

    private void tryMoveLR() {
        int offset = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 10 : 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            controller.moveCamera(-offset, 0, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            controller.moveCamera(offset, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            controller.moveCamera(0, offset, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            controller.moveCamera(0, -offset, 0);
        }
    }

    private void tryMoveUD() {
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
