package stonering.game.controller.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import stonering.game.controller.controllers.toolbar.DesignationsController;
import stonering.game.controller.inputProcessors.*;
import stonering.util.global.Logger;

/**
 * Container for all controllers. Also creates and registers all InputProcessors.
 *
 * @author Alexander Kuzyakov on 26.06.2017.
 */
public class GameController extends Controller {
    private DesignationsController designationsController;
    private InputMultiplexer inputMultiplexer;
    private EntitySelectorInputAdapter entitySelectorInputAdapter;
    private PauseInputAdapter pauseInputAdapter;

    public void init() {
        super.init();
        inputMultiplexer = new InputMultiplexer();
        designationsController = new DesignationsController();
        pauseInputAdapter = new PauseInputAdapter(gameMvc);
        entitySelectorInputAdapter = new EntitySelectorInputAdapter();
        designationsController.init();
        inputMultiplexer.addProcessor(new KeyBufferInputAdapter());                 // only buffers events
        inputMultiplexer.addProcessor(pauseInputAdapter);                           // handles pause
        inputMultiplexer.addProcessor(new StageInputAdapter(gameMvc.getView()));    // calls stages
        inputMultiplexer.addProcessor(entitySelectorInputAdapter);                  // calls entity selector (model camera)
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public DesignationsController getDesignationsController() {
        return designationsController;
    }


    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }

    public void setCameraEnabled(boolean value) {
        if(entitySelectorInputAdapter != null) {
            entitySelectorInputAdapter.setEnabled(value);
        } else {
            Logger.UI.logDebug("Changing EntitySelectorInputAdapter state before GameController init.");
        }
    }

    public PauseInputAdapter getPauseInputAdapter() {
        return pauseInputAdapter;
    }
}
