/*
 * Created by Alexander on .
 */

/*
 * Created by Alexander on .
 */

package stonering.game.core.controller.controllers;

import stonering.game.core.model.GameContainer;
import stonering.game.core.view.GameView;

/**
 * Controller for camera. Works with GameContainer directly.
 *
 * Created by Alexander on 25.12.2017.
 */
public class CameraConroller extends Controller{

    public CameraConroller(GameContainer container, GameView view) {
        super(container, view);
    }

    /**
     * Moves camera over local map. Game frame rendered around it.
     * @param dx
     * @param dy
     * @param dz
     */
    public void moveCamera(int dx, int dy, int dz) {
        container.getCamera().moveCamera(dx, dy, dz);
    }
}
