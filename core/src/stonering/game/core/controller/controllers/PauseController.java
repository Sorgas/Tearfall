package stonering.game.core.controller.controllers;

import stonering.game.core.GameMvc;
import stonering.game.core.controller.inputProcessors.PauseInputAdapter;
import stonering.game.core.model.GameContainer;

/**
 * Pauses and unpauses game timer. Works with GameContainer directly.
 * Used from Gdx.input and from game events.
 * Handles events from {@link PauseInputAdapter}
 *
 * @author Alexander Kuzyakov on 25.12.2017.
 */
public class PauseController extends Controller{
    private GameContainer container;
    private boolean enabled;

    public PauseController(GameMvc gameMvc) {
        super(gameMvc);
    }

    public void switchPause() {
        container.setPaused(container.isPaused());
    }

    public void pause() {
        container.setPaused(true);
    }

    public void unpause() {
        container.setPaused(false);
    }

    @Override
    public void init() {
        container = gameMvc.getModel();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPaused() {
        return container.isPaused();
    }
}
