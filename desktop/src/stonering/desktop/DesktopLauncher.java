package stonering.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import stonering.TearFall;
import stonering.util.logging.Logger;

/**
 * Entry point for the game.
 */
public class DesktopLauncher {

    public static void main(String[] arg) {
        Logger.GENERAL.logDebug("Main launcher.");
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1920;
        config.height = 1080;
        config.title = "TearFall";
        config.foregroundFPS = 30;
        Game game = new TearFall();
        Logger.enableAll();
        new LwjglApplication(game, config);
    }
}