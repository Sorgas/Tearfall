package stonering.game.controller.inputProcessors;

import com.badlogic.gdx.InputAdapter;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.util.global.Logger;

/**
 * Can be disabled.
 *
 * @author Alexander Kuzyakov on 29.11.2017.
 */
public class PauseInputAdapter extends InputAdapter {
    private boolean enabled = true;

    @Override
    public boolean keyTyped(char character) {
        if (enabled && character == ' ') switchPause();
        return false;
    }

    private void switchPause() {
        GameModel model = GameMvc.instance().getModel();
        model.setPaused(!model.isPaused());
        Logger.GENERAL.logDebug(model.isPaused() ? "Pausing game" : "Unpausing game");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}