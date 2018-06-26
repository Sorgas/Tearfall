package stonering.game.core.controller.controllers;

import stonering.game.core.GameMvc;
import stonering.game.core.model.GameContainer;

/**
 * Controller for camera. Works with GameContainer directly.
 *
 * @author Alexander Kuzyakov on 25.12.2017.
 */
public class CameraConroller extends Controller{
    private GameContainer container;

    public CameraConroller(GameMvc gameMvc) {
        super(gameMvc);
    }

    @Override
    public void init() {
        container = gameMvc.getModel();
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
