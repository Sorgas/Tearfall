package stonering.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import org.lwjgl.LWJGLException;

import stonering.test_chamber.TestChamberGame;
import stonering.util.logging.Logger;

/**
 * @author Alexander on 30.10.2018.
 */
public class TestChamberLauncher {

    public static void main(String[] arg) throws LWJGLException {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1920;
        config.height = 1080;
        config.title = "TearFall";
        config.foregroundFPS = 60;
        Game game = new TestChamberGame();
        Logger.enableAll();
        new LwjglApplication(game, config);
    }
}
