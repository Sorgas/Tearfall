package stonering.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import stonering.TearFall;
import stonering.util.global.TagLoggersEnum;

public class DesktopLauncher {

    public static void main(String[] arg) {
        System.out.println("main");
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 120;
        config.height = 60;
        config.title = "TearFall";
        config.foregroundFPS = 30;
        Game game = new TearFall();
        TagLoggersEnum.enableAll();
//        TagLoggersEnum.UI.setEnabled(false);
        new LwjglApplication(game, config);
    }
}