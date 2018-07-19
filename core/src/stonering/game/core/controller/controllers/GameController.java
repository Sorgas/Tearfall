package stonering.game.core.controller.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.inputProcessors.*;

/**
 * Container for all controllers. Also creates and registers all InputProcessors.
 *
 * @author Alexander Kuzyakov on 26.06.2017.
 */
public class GameController extends Controller {
    private DesignationsController designationsController;
    private CameraConroller cameraConroller;
    private PauseController pauseController;
    private ToolBarController toolBarController;
    private InputMultiplexer inputMultiplexer;
    private UIController uiController;

    public GameController(GameMvc gameMvc) {
        super(gameMvc);
        inputMultiplexer = new InputMultiplexer();
        designationsController = new DesignationsController(gameMvc);
        cameraConroller = new CameraConroller(gameMvc);
        pauseController = new PauseController(gameMvc);
        toolBarController = new ToolBarController(gameMvc);
        uiController = new UIController(gameMvc);
    }

    public void init() {
        toolBarController.init();
        cameraConroller.init();
        designationsController.init();
        pauseController.init();
        uiController.init();

        inputMultiplexer.addProcessor(new CameraInputProcessor(this));
        inputMultiplexer.addProcessor(new PauseInputProcessor(this));
        inputMultiplexer.addProcessor(new ToolBarInputProcessor(this));
//        inputMultiplexer.addProcessor(new DesignationsInputProcessor(this));
        inputMultiplexer.addProcessor(new ListInputProcessor(this));
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

    public UIController getUiController() {
        return uiController;
    }
}
