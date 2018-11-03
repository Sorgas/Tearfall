package stonering.game.core.controller.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import stonering.game.core.GameMvc;
import stonering.game.core.model.GameCamera;
import stonering.game.core.view.GameView;
import stonering.game.core.view.render.ui.components.menus.util.Invokable;
import stonering.game.core.view.render.ui.components.menus.util.MouseInvocable;

import java.util.Arrays;

/**
 * Handles all input in the game.
 * First priority are menus in iuDrawer, then camera.
 *
 * @author Alexander on 06.09.2018.
 */
public class GameInputHandler extends Controller implements Invokable {
    private GameView gameView;
    private GameCamera camera;

    public GameInputHandler(GameMvc gameMvc) {
        super(gameMvc);
    }

    @Override
    public void init() {
        gameView = gameMvc.getView();
        camera = gameMvc.getModel().getCamera();
    }

    public void handleEvent(int screenX, int screenY, int button, int action) {
        Invokable menu = gameView.getUiDrawer().getToolbar().getActiveMenu();
        if (Arrays.asList(menu.getClass().getInterfaces()).contains(MouseInvocable.class)) {
            Vector2 vector = gameView.getWorldDrawer().translateScreenPositionToModel(new Vector2(screenX, screenY));
            ((MouseInvocable) menu).invoke(Math.round(vector.x), Math.round(vector.y), button, action);
        }
    }

    @Override
    public boolean invoke(int keycode) {
        if (!gameView.invoke(keycode)) {
            tryMoveCamera();
        }
        return true;
    }

    public void tryMoveCamera() {
        int offset = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 10 : 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.moveCamera(-offset, 0, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.moveCamera(offset, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.moveCamera(0, offset, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.moveCamera(0, -offset, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            camera.moveCamera(0, 0, 1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            camera.moveCamera(0, 0, -1);
        }
    }
}
