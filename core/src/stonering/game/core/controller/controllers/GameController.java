package stonering.game.core.controller.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.toolbar.DesignationsController;
import stonering.game.core.controller.inputProcessors.*;
import stonering.utils.global.TagLoggersEnum;

/**
 * Container for all controllers. Also creates and registers all InputProcessors.
 *
 * @author Alexander Kuzyakov on 26.06.2017.
 */
public class GameController extends Controller {
    private DesignationsController designationsController;
    private PauseController pauseController;
    private InputMultiplexer inputMultiplexer;
    private CameraInputAdapter cameraInputAdapter;

    public GameController(GameMvc gameMvc) {
        super(gameMvc);
        inputMultiplexer = new InputMultiplexer();
        designationsController = new DesignationsController(gameMvc);
        pauseController = new PauseController(gameMvc);
    }

    public void init() {
        designationsController.init();
        pauseController.init();
        cameraInputAdapter = new CameraInputAdapter(gameMvc);

        inputMultiplexer.addProcessor(new KeyBufferInputAdapter());                 // only buffers events
        inputMultiplexer.addProcessor(new PauseInputAdapter(pauseController));      // handles pause
        inputMultiplexer.addProcessor(new StageInputAdapter(gameMvc.getView()));    // calls stages
        inputMultiplexer.addProcessor(cameraInputAdapter);                          // calls entity selector (camera)
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

    public void setCameraEnabled(boolean value) {
        if(cameraInputAdapter != null) {
            cameraInputAdapter.setEnabled(value);
        } else {
            TagLoggersEnum.UI.logDebug("Changing CameraInputAdapter state before GameController init.");
        }
    }
}
