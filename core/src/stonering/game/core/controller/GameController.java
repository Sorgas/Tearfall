package stonering.game.core.controller;

import stonering.game.core.model.GameContainer;
import stonering.utils.Position;

/**
 * Created by Alexander on 26.06.2017.
 */
public class GameController {
    private Position camera;
    private GameContainer container;


    public GameController(GameContainer container) {
        this.container = container;
        camera = new Position(container.getMap().getxSize() / 2, container.getMap().getySize() / 2, 10);
    }

    public Position getCamera() {
        return camera;
    }
}
