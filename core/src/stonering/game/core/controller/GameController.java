package stonering.game.core.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.controller.inputProcessors.CameraInputProcessor;
import stonering.game.core.controller.inputProcessors.PauseInputProcessor;
import stonering.game.core.model.GameContainer;
import stonering.global.utils.Position;
import stonering.utils.global.NavigationInputBuffer;

/**
 * Created by Alexander on 26.06.2017.
 */
public class GameController {
    private Position camera;
    private GameContainer container;
    private InputMultiplexer inputMultiplexer;

    public GameController(GameContainer container) {
        this.container = container;
        initCamera();
        initInputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void initInputMultiplexer() {
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new PauseInputProcessor(this));
        inputMultiplexer.addProcessor(new CameraInputProcessor(this, new NavigationInputBuffer(4)));
    }

    private void initCamera() {
        camera = new Position(container.getLocalMap().getxSize() / 2, container.getLocalMap().getySize() / 2, container.getLocalMap().getzSize() - 1);
        while (container.getLocalMap().getBlockType(camera.getX(), camera.getY(), camera.getZ()) == BlockTypesEnum.SPACE.getCode()) {
            camera.setZ(camera.getZ() - 1);
        }
    }

    public Position getCamera() {
        return camera;
    }

    public void moveCamera(int dx, int dy, int dz) {
        if ((camera.getX() >= 0 && dx < 0) || (camera.getX() < container.getLocalMap().getxSize() && dx > 0)) {
            camera.setX(camera.getX() + dx);
        }
        if ((camera.getY() >= 0 && dy < 0) || (camera.getY() < container.getLocalMap().getySize() && dy > 0)) {
            camera.setY(camera.getY() + dy);
        }
        if ((camera.getZ() >= 0 && dz < 0) || (camera.getZ() < container.getLocalMap().getzSize() && dz > 0)) {
            camera.setZ(camera.getZ() + dz);
        }
        System.out.println(camera.toString());
    }

    public void switchPause() {
        container.pauseGame();
    }

    public void pause() {
        container.pauseGame(true);
    }

    public void unpause() {
        container.pauseGame(false);
    }
}
