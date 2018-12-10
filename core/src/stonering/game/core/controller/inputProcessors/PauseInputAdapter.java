package stonering.game.core.controller.inputProcessors;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import stonering.game.core.controller.controllers.GameController;
import stonering.game.core.controller.controllers.PauseController;
import stonering.global.settings.KeySettings;

/**
 * @author Alexander Kuzyakov on 29.11.2017.
 */
public class PauseInputAdapter extends InputAdapter {
    private PauseController controller;

    public PauseInputAdapter(PauseController controller) {
        this.controller = controller;
    }

    @Override
    public boolean keyTyped(char character) {
        if (character == ' ') {
            controller.switchPause();
        }
        return false;
    }
}