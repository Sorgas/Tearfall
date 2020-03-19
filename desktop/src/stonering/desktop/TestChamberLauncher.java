package stonering.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFrame;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import stonering.test_chamber.TestChamberGame;
import stonering.util.global.Logger;

/**
 * @author Alexander on 30.10.2018.
 */
public class TestChamberLauncher {

    public static void main(String[] arg) throws LWJGLException {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 800;
        config.height = 600;
        config.title = "TearFall";
        config.foregroundFPS = 30;
        Game game = new TestChamberGame();
        Logger.enableAll();
        new LwjglApplication(game, config);
    }
}
