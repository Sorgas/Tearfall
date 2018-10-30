package stonering.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import stonering.game.MockGame;

/**
 * @author Alexander on 30.10.2018.
 */
public class DesktopMenuLauncher {

    public static void main(String[] arg) {
        System.out.println("main");
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1600;
        config.height = 900;
        config.title = "TearFall";
        config.foregroundFPS = 30;
        Game game = new MockGame();
        new LwjglApplication(game, config);
    }
}
