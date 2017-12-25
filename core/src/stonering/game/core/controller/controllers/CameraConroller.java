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
 * Created by Alexander on 25.12.2017.
 *
 * works with GAmeContainer directly
 */
public class CameraConroller extends Controller{

    public CameraConroller(GameContainer container, GameView view) {
        super(container, view);
    }

    public void moveCamera(int dx, int dy, int dz) {
        container.getCamera().moveCamera(dx, dy, dz);
    }
}
