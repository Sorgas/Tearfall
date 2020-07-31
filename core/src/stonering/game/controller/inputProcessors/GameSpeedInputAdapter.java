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
                GameMvc.model().gameTime.setGameSpeed(1);
                return true;
            case Input.Keys.NUM_2:
                GameMvc.model().gameTime.setGameSpeed(2);
                return true;
            case Input.Keys.NUM_3:
                GameMvc.model().gameTime.setGameSpeed(4);
                return true;
            case Input.Keys.PERIOD:
                GameMvc.model().gameTime.singleUpdate();
                return true;
        }
        return false;
    }
}
