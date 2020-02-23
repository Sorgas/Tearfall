package stonering.game.controller.inputProcessors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import stonering.game.GameMvc;

/**
 * Adapter for opening main menu from game.
 *
 * @author Alexander on 26.11.2019.
 */
public class MainMenuInputAdapter extends InputAdapter {
    @Override
    public boolean keyDown(int keycode) {
        if (keycode != Input.Keys.ESCAPE) return false;
        GameMvc.view().showPauseMenu();
        return true;
    }
}
