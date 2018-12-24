package stonering.game.core.controller.inputProcessors;

import com.badlogic.gdx.InputAdapter;
import stonering.game.core.GameMvc;

/**
 * Can be disabled.
 *
 * @author Alexander Kuzyakov on 29.11.2017.
 */
public class PauseInputAdapter extends InputAdapter {
    private GameMvc gameMvc;
    private boolean enabled = true;

    public PauseInputAdapter(GameMvc gameMvc) {
        this.gameMvc = gameMvc;
    }

    @Override
    public boolean keyTyped(char character) {
        if (enabled && character == ' ') {
            gameMvc.getModel().setPaused(!gameMvc.getModel().isPaused());
        }
        return false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}