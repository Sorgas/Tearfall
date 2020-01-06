package stonering.game.controller.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import stonering.game.GameMvc;
import stonering.game.controller.inputProcessors.*;
import stonering.util.global.Logger;

/**
 * Container for all controllers. Also creates and registers all InputProcessors.
 *
 * @author Alexander Kuzyakov on 26.06.2017.
 */
public class GameController {
    public InputMultiplexer inputMultiplexer;
    public EntitySelectorInputAdapter entitySelectorInputAdapter;
    public PauseInputAdapter pauseInputAdapter;
    public MainMenuInputAdapter mainMenuInputAdapter;

    public void init() {
        Gdx.input.setInputProcessor(createInputMultiplexer());
    }

    private InputMultiplexer createInputMultiplexer() {
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new KeyBufferInputAdapter());                                   // only buffers events
        inputMultiplexer.addProcessor(pauseInputAdapter = new PauseInputAdapter());                   // handles pause
        inputMultiplexer.addProcessor(GameMvc.instance().view().stageInputAdapter);                   // calls stages (menus hotkeys)
        inputMultiplexer.addProcessor(entitySelectorInputAdapter = new EntitySelectorInputAdapter()); // calls entity selector (movement)
        inputMultiplexer.addProcessor(mainMenuInputAdapter = new MainMenuInputAdapter());             // opens main menu
        return inputMultiplexer;
    }

    public void setSelectorEnabled(boolean value) {
        if(entitySelectorInputAdapter != null) {
            Logger.UI.logDebug((value ? "Enabling" : "Disabling") + " EntitySelectorInputAdapter.");
            entitySelectorInputAdapter.setEnabled(value);
        } else {
            Logger.UI.logDebug("Changing EntitySelectorInputAdapter progress before GameController init.");
        }
    }
}
