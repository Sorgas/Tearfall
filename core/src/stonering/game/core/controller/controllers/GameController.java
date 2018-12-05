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

    public GameController(GameMvc gameMvc) {
        super(gameMvc);
        inputMultiplexer = new InputMultiplexer();
        designationsController = new DesignationsController(gameMvc);
        pauseController = new PauseController(gameMvc);
    }

    public void init() {
        designationsController.init();
        pauseController.init();

        inputMultiplexer.addProcessor(new KeyBufferInputAdapter());                 // only buffers events
        inputMultiplexer.addProcessor(new PauseInputProcessor(this));      // handles pause
        inputMultiplexer.addProcessor(new StageInputAdapter(gameMvc.getView()));    // calls stages
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public DesignationsController getDesignationsController() {
        return designationsController;
    }

    public PauseController getPauseController() {
        return pauseController;
    }

    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }
}
