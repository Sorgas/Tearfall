package stonering.game.core.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import stonering.game.core.controller.inputProcessors.CameraInputProcessor;
import stonering.game.core.model.GameContainer;
import stonering.global.NavigationInputBuffer;
import stonering.utils.Position;

/**
 * Created by Alexander on 26.06.2017.
 */
public class GameController {
    private Position camera;
    private GameContainer container;
    private InputProcessor activeInputProcessor;
    private CameraInputProcessor cameraInputProcessor;

    private NavigationInputBuffer navigationInputBuffer;

    public GameController(GameContainer container) {
        this.container = container;
        camera = new Position(container.getLocalMap().getxSize() / 2, container.getLocalMap().getySize() / 2, 4);//container.getLocalMap().getzSize() / 2);
        navigationInputBuffer = new NavigationInputBuffer(4);
        cameraInputProcessor = new CameraInputProcessor(this, navigationInputBuffer);
        Gdx.input.setInputProcessor(cameraInputProcessor);
    }

    public void fetchInput() {
    }

    public Position getCamera() {
        return camera;
    }

    public void moveCamera(int dx, int dy, int dz) {
        if ((camera.getX() > 0 && dx < 0) || (camera.getX() < (container.getLocalMap().getxSize() - 1) && dx > 0)) {
            camera.setX(camera.getX() + dx);
        }
        if ((camera.getY() > 0 && dy < 0) || (camera.getY() < (container.getLocalMap().getySize() - 1) && dy > 0)) {
            camera.setY(camera.getY() + dy);
        }
        if ((camera.getZ() > 0 && dz < 0) || (camera.getZ() < (container.getLocalMap().getzSize() - 1) && dz > 0)) {
            camera.setZ(camera.getZ() + dz);
        }
        System.out.println(camera.toString() + " " + container.getLocalMap().getBlockType(camera.getX(), camera.getY(), camera.getZ()));
    }
}
