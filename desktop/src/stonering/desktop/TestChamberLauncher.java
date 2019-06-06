package stonering.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import stonering.test_chamber.TestChamberGame;
import stonering.util.global.TagLoggersEnum;

/**
 * @author Alexander on 30.10.2018.
 */
public class TestChamberLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1600;
        config.height = 900;
        config.title = "TearFall";
        config.foregroundFPS = 30;
        Game game = new TestChamberGame();
        TagLoggersEnum.enableAll();
        new LwjglApplication(game, config);
    }
}
