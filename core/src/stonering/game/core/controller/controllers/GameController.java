package stonering.game.core.controller.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Pools;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.toolbar.DesignationsController;
import stonering.game.core.controller.inputProcessors.*;
import stonering.util.global.TagLoggersEnum;

/**
 * Container for all controllers. Also creates and registers all InputProcessors.
 *
 * @author Alexander Kuzyakov on 26.06.2017.
 */
public class GameController extends Controller {
    private DesignationsController designationsController;
    private InputMultiplexer inputMultiplexer;
    private CameraInputAdapter cameraInputAdapter;
    private PauseInputAdapter pauseInputAdapter;

    public void init() {
        super.init();
        inputMultiplexer = new InputMultiplexer();
        designationsController = new DesignationsController();
        pauseInputAdapter = new PauseInputAdapter(gameMvc);
        cameraInputAdapter = new CameraInputAdapter(gameMvc);
        designationsController.init();
        inputMultiplexer.addProcessor(new KeyBufferInputAdapter());                 // only buffers events
        inputMultiplexer.addProcessor(pauseInputAdapter);                           // handles pause
        inputMultiplexer.addProcessor(new StageInputAdapter(gameMvc.getView()));    // calls stages
        inputMultiplexer.addProcessor(cameraInputAdapter);                          // calls entity selector (camera)
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public DesignationsController getDesignationsController() {
        return designationsController;
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

    public PauseInputAdapter getPauseInputAdapter() {
        return pauseInputAdapter;
    }
}
