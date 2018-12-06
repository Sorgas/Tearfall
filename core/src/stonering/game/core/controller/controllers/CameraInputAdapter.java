package stonering.game.core.controller.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import stonering.game.core.GameMvc;
import stonering.game.core.model.EntitySelector;

/**
 * Handles button presses for camera navigation.
 * Is part of {@link KeyBufferInputAdapter}
 *
 * @author Alexander Kuzyakov
 */
public class CameraInputAdapter extends InputAdapter {
    private GameMvc gameMvc;
    private EntitySelector camera;

    public CameraInputAdapter(GameMvc gameMvc) {
        this.gameMvc = gameMvc;
        camera = gameMvc.getModel().getCamera();
    }

    @Override
    public boolean keyDown(int keycode) {
        int offset = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 10 : 1;
        switch (keycode) {
            case Input.Keys.W:
                camera.moveSelector(0, offset, 0);
                return true;
            case Input.Keys.A:
                camera.moveSelector(-offset, 0, 0);
                return true;
            case Input.Keys.S:
                camera.moveSelector(0, -offset, 0);
                return true;
            case Input.Keys.D:
                camera.moveSelector(offset, 0, 0);
                return true;
            case Input.Keys.R:
                camera.moveSelector(0, 0, 1);
                return true;
            case Input.Keys.F:
                camera.moveSelector(0, 0, -1);
                return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        switch (charToKeycode(character)) {
            case Input.Keys.W:
            case Input.Keys.A:
            case Input.Keys.S:
            case Input.Keys.D:
                int offset = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 10 : 1;
                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                    camera.moveSelector(0, offset, 0);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    camera.moveSelector(0, -offset, 0);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    camera.moveSelector(-offset, 0, 0);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    camera.moveSelector(offset, 0, 0);
                }
                return true;
            case Input.Keys.R:
                camera.moveSelector(0, 0, 1);
                return true;
            case Input.Keys.F:
                camera.moveSelector(0, 0, -1);
                return true;
        }
        return false;
    }

    private int charToKeycode(char character) {
        return Input.Keys.valueOf(String.valueOf(character).toUpperCase());
    }
}
