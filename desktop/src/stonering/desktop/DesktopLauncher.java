package stonering.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import stonering.TearFall;
import stonering.util.global.Logger;

public class DesktopLauncher {

    public static void main(String[] arg) {
        System.out.println("main");
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 200;
        config.height = 150;
        config.title = "TearFall";
        config.foregroundFPS = 30;
        Game game = new TearFall();
        Logger.enableAll();
//        Logger.UI.setEnabled(false);
        new LwjglApplication(game, config);
    }
}