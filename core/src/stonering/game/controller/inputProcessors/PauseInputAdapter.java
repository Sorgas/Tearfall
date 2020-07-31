package stonering.game.controller.inputProcessors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.util.logging.Logger;

/**
 * Can be disabled.
 *
 * @author Alexander Kuzyakov on 29.11.2017.
 */
public class PauseInputAdapter extends InputAdapter {
    public boolean enabled = true;

    @Override
    public boolean keyDown(int keycode) {
        if (keycode != Input.Keys.SPACE) return false;
        Logger.INPUT.logDebug("Handling SPACE in " + (enabled ? "enabled" : "disabled") + " PauseInputAdapter");
        if (enabled) switchPause();
        return true;
    }

    private void switchPause() {
        GameModel model = GameMvc.model();
        model.gameTime.setPaused(!model.gameTime.paused);
    }
}