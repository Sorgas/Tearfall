package stonering.game.controller.inputProcessors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import stonering.game.GameMvc;

/**
 * @author Alexander on 6/15/2020
 */
public class GameSpeedInputAdapter extends InputAdapter {

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.NUM_1:
                GameMvc.model().setGameSpeed(1);
                return true;
            case Input.Keys.NUM_2:
                GameMvc.model().setGameSpeed(2);
                return true;
            case Input.Keys.NUM_3:
                GameMvc.model().setGameSpeed(4);
                return true;
        }
        return false;
    }
}
