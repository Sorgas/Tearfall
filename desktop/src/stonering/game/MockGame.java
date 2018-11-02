package stonering.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import stonering.screens.WorkbenchMenuScreen;

/**
 * Mock game with one screen.
 *
 * @author Alexander on 30.10.2018.
 */
public class MockGame extends Game {
    private Screen screen;

    @Override
    public void create() {
        screen = new WorkbenchMenuScreen();
        setScreen(screen);
    }
}
