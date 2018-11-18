package stonering.game.core.controller.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import stonering.game.core.GameMvc;
import stonering.game.core.model.GameCamera;

/**
 * Handles button presses for camera navigation.
 * Is part of {@link GameInputHandler}
 *
 * @author Alexander Kuzyakov
 */
public class CameraInputHandler {
    private GameMvc gameMvc;
    private GameCamera camera;

    public CameraInputHandler(GameMvc gameMvc) {
        this.gameMvc = gameMvc;
        camera = gameMvc.getModel().getCamera();
    }

    /**
     * On keyDown.
     *
     * @param keycode
     */
    public void tryMoveCamera(int keycode) {
        int offset = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 10 : 1;
        switch (keycode) {
            case Input.Keys.W:
                camera.moveCamera(0, offset, 0);
                break;
            case Input.Keys.A:
                camera.moveCamera(-offset, 0, 0);
                break;
            case Input.Keys.S:
                camera.moveCamera(0, -offset, 0);
                break;
            case Input.Keys.D:
                camera.moveCamera(offset, 0, 0);
                break;
            case Input.Keys.R:
                camera.moveCamera(0, 0, 1);
                break;
            case Input.Keys.F:
                camera.moveCamera(0, 0, -1);
                break;
        }
    }

    public boolean typeCameraKey(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                observePressedWasd();
                break;
            case Input.Keys.A:
                observePressedWasd();
                break;
            case Input.Keys.S:
                observePressedWasd();
                break;
            case Input.Keys.D:
                observePressedWasd();
                break;
            case Input.Keys.R:
                camera.moveCamera(0, 0, 1);
                break;
            case Input.Keys.F:
                camera.moveCamera(0, 0, -1);
                break;
        }
        return true;
    }


    private void observePressedWasd() {
        int offset = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 10 : 1;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.moveCamera(0, offset, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.moveCamera(0, -offset, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.moveCamera(-offset, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.moveCamera(offset, 0, 0);
        }
    }

    private int charToKeycode(char character) {
        return Input.Keys.valueOf(String.valueOf(character).toUpperCase());
    }
}
