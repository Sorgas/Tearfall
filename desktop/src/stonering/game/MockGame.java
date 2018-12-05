package stonering.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import stonering.game.core.GameMvc;
import stonering.screens.WorkbenchMenuScreen;

/**
 * Mock game with one screen.
 *
 * @author Alexander on 30.10.2018.
 */
public class MockGame extends Game {
    private Screen screen;
    private GameMvc gameMvc;

    @Override
    public void create() {
        screen = new WorkbenchMenuScreen();
        setScreen(screen);
    }

    private void createGameMvc() {

    }
}
