package stonering.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import stonering.TearFall;
import stonering.utils.global.TaggedLogger;

import java.util.Arrays;

public class DesktopLauncher {

    public static void main(String[] arg) {
        System.out.println("main");
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1600;
        config.height = 900;
        config.title = "TearFall";
        config.foregroundFPS = 30;
        Game game = new TearFall();
        Application application = new LwjglApplication(game, config);
        application.setApplicationLogger(new TaggedLogger(Arrays.asList("tasks")));
    }
}