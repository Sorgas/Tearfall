package stonering.game.core.controller.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.toolbar.DesignationsController;
import stonering.game.core.controller.inputProcessors.*;

/**
 * Container for all controllers. Also creates and registers all InputProcessors.
 *
 * @author Alexander Kuzyakov on 26.06.2017.
 */
public class GameController extends Controller {
    private DesignationsController designationsController;
    private PauseController pauseController;
    private InputMultiplexer inputMultiplexer;
    private UIController uiController;

    private GameInputHandler gameInputHandler;

    public GameController(GameMvc gameMvc) {
        super(gameMvc);
        inputMultiplexer = new InputMultiplexer();
        designationsController = new DesignationsController(gameMvc);
        pauseController = new PauseController(gameMvc);
        uiController = new UIController(gameMvc);
        gameInputHandler = new GameInputHandler(gameMvc);
    }

    public void init() {
        designationsController.init();
        pauseController.init();
        uiController.init();
        gameInputHandler.init();

        inputMultiplexer.addProcessor(new PauseInputProcessor(this));
        inputMultiplexer.addProcessor(new GameInputProcessor(this));
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public DesignationsController getDesignationsController() {
        return designationsController;
    }

    public PauseController getPauseController() {
        return pauseController;
    }

    public GameInputHandler getGameInputHandler() {
        return gameInputHandler;
    }
}
