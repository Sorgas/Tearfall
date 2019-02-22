package stonering.game.core.controller.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import stonering.game.core.GameMvc;

/**
 * Input adapter for camera, used for zooming {@link stonering.game.core.view.render.stages.base.BaseStage}
 *
 * @author Alexander on 19.02.2019.
 */
public class CameraInputAdapter extends InputAdapter {
    private GameMvc gameMvc;

    public CameraInputAdapter() {
        gameMvc = GameMvc.getInstance();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.PLUS:
            case Input.Keys.MINUS: {
                return gameMvc.getView().getBaseStage().keyDown(keycode);
            }
        }
        return false;
    }
}
