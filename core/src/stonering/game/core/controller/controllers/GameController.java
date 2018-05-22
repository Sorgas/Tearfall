package stonering.game.core.controller.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import stonering.game.core.controller.inputProcessors.CameraInputProcessor;
import stonering.game.core.controller.inputProcessors.DesignationsInputProcessor;
import stonering.game.core.controller.inputProcessors.PauseInputProcessor;
import stonering.game.core.controller.inputProcessors.ToolBarInputProcessor;
import stonering.game.core.model.GameContainer;
import stonering.game.core.view.GameView;

/**
 * Container for all controllers.
 *
 * Created by Alexander on 26.06.2017.
 */
public class GameController extends Controller {
    private DesignationsController designationsController;
    private CameraConroller cameraConroller;
    private PauseController pauseController;
    private ToolBarController toolBarController;

    private InputMultiplexer inputMultiplexer;

    public GameController(GameContainer container, GameView view) {
        super(container, view);
        inputMultiplexer = new InputMultiplexer();
    }

    public void init() {
        initControllers();
        initInputMultiplexer();
    }

    private void initControllers() {
        designationsController = new DesignationsController(container, view);
        cameraConroller = new CameraConroller(container, view);
        pauseController = new PauseController(container, view);
        toolBarController = new ToolBarController(container, view);
    }

    private void initInputMultiplexer() {
        inputMultiplexer.addProcessor(new ToolBarInputProcessor(this));
        inputMultiplexer.addProcessor(new PauseInputProcessor(this));
        inputMultiplexer.addProcessor(new CameraInputProcessor(this));
        inputMultiplexer.addProcessor(new DesignationsInputProcessor(this));
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void addInputProcessor(InputAdapter adapter) {
        inputMultiplexer.addProcessor(adapter);
    }

    public DesignationsController getDesignationsController() {
        return designationsController;
    }

    public CameraConroller getCameraConroller() {
        return cameraConroller;
    }

    public PauseController getPauseController() {
        return pauseController;
    }

    public ToolBarController getToolBarController() {
        return toolBarController;
    }
}
