package com;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.frames.GameFrame;

public class DesktopLauncher {

	public static void main (String[] arg) {
		System.out.println("main");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1100;
		config.height = 1100;
		config.title = "Dwarf Destiny";
        config.foregroundFPS = 3;
		GameFrame frame = new GameFrame();
		new GameMvcInitializer().initGame(frame);
		new LwjglApplication(frame, config);
	}
}
