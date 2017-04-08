package com;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

	public static void main (String[] arg) {
		System.out.println("main");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1400;
		config.height = 800;
		config.title = "TearFall";
        config.foregroundFPS = 5;
		Game game = new TearFall();
		new LwjglApplication(game, config);
	}
}
