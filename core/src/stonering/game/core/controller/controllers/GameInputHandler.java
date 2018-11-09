package stonering.game.core.controller.controllers;

import com.badlogic.gdx.math.Vector2;
import stonering.game.core.GameMvc;
import stonering.game.core.view.GameView;
import stonering.game.core.view.render.ui.menus.util.Invokable;

/**
 * Handles all input in the game.
 * First priority are menus in iuDrawer, then camera.
 *
 * @author Alexander on 06.09.2018.
 */
public class GameInputHandler extends Controller implements Invokable {
    private GameView gameView;
    private CameraInputHandler cameraInputHandler;

    public GameInputHandler(GameMvc gameMvc) {
        super(gameMvc);
        cameraInputHandler = new CameraInputHandler(gameMvc);
    }

    @Override
    public void init() {
        gameView = gameMvc.getView();
    }

    public boolean handleEvent(int screenX, int screenY, int button, int action) {
        Vector2 vector2 = gameView.getWorldDrawer().translateScreenPositionToModel(new Vector2(screenX, screenY));
        return gameView.invoke(Math.round(vector2.x), Math.round(vector2.y), button, action);
    }

    /**
     * Invoked on keyDown.
     *
     * @param keycode
     * @return
     */
    @Override
    public boolean invoke(int keycode) {
        if (!gameView.invoke(keycode)) { // no ui is active
            cameraInputHandler.tryMoveCamera(keycode);
        }
        return true;
    }

    public boolean typed(char character) {
        return cameraInputHandler.typeCameraKey(character);
    }
}
