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
    public DesignationsController designationsController;
    public InputMultiplexer inputMultiplexer;
    public EntitySelectorInputAdapter entitySelectorInputAdapter;
    public PauseInputAdapter pauseInputAdapter;

    public void init() {
        super.init();
        inputMultiplexer = new InputMultiplexer();
        designationsController = new DesignationsController();
        pauseInputAdapter = new PauseInputAdapter();
        entitySelectorInputAdapter = new EntitySelectorInputAdapter();
        designationsController.init();
        inputMultiplexer.addProcessor(new KeyBufferInputAdapter());                 // only buffers events
        inputMultiplexer.addProcessor(pauseInputAdapter);                           // handles pause
        inputMultiplexer.addProcessor(gameMvc.getView().stageInputAdapter);                           // calls stages
        inputMultiplexer.addProcessor(entitySelectorInputAdapter);                  // calls entity selector (movement, selection)
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void setCameraEnabled(boolean value) {
        if(entitySelectorInputAdapter != null) {
            Logger.UI.logDebug(value ? "Enabling" : "Disabling" + " EntitySelectorInputAdapter.");
            entitySelectorInputAdapter.setEnabled(value);
        } else {
            Logger.UI.logDebug("Changing EntitySelectorInputAdapter state before GameController init.");
        }
    }
}
