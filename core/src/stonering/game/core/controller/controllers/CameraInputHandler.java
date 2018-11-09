package stonering.game.core.controller.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import stonering.game.core.GameMvc;
import stonering.game.core.model.GameCamera;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Handles button presses for camera navigation.
 * Is part of {@link GameInputHandler}
 *
 * @author Alexander Kuzyakov
 */
public class CameraInputHandler {
    private GameMvc gameMvc;
    private GameCamera camera;
    private final static ArrayList<Character> cameraHorizontalMove = new ArrayList<>(Arrays.asList('a', 's', 'd', 'w', 'A', 'S', 'D', 'W', 'r', 'f', 'R', 'F'));
    private boolean W;
    private boolean A;
    private boolean S;
    private boolean D;

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
                W = true;
                break;
            case Input.Keys.A:
                camera.moveCamera(-offset, 0, 0);
                A = true;
                break;
            case Input.Keys.S:
                camera.moveCamera(0, -offset, 0);
                S = true;
                break;
            case Input.Keys.D:
                camera.moveCamera(offset, 0, 0);
                D = true;
                break;
        }
    }

    public boolean typeCameraKey(char character) {
        if (cameraHorizontalMove.contains(character)) {
            switch (charToKeycode(character)) {
                case Input.Keys.W:
                    if (W) {
                        W = false;
                    } else {
                        observePressedWasd();
                    }
                    break;
                case Input.Keys.A:
                    if (A) {
                        A = false;
                    } else {
                        observePressedWasd();
                    }
                    break;
                case Input.Keys.S:
                    if (S) {
                        S = false;
                    } else {
                        observePressedWasd();
                    }
                    break;
                case Input.Keys.D:
                    if (D) {
                        D = false;
                    } else {
                        observePressedWasd();
                    }
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
        return false;
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
