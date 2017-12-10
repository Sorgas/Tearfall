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
    private GameContainer container;
    private InputMultiplexer inputMultiplexer;

    public GameController(GameContainer container) {
        this.container = container;
        initInputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void initInputMultiplexer() {
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new PauseInputProcessor(this));
        inputMultiplexer.addProcessor(new CameraInputProcessor(this, new NavigationInputBuffer(4)));
    }

    public void moveCamera(int dx, int dy, int dz) {
        container.getCamera().moveCamera(dx, dy, dz);
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
