package com;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.frames.GameFrame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 2000;
		config.height = 900;
		config.title = "Dwarf Destiny";
		GameFrame frame = new GameFrame();
		new GameMvcInitializer().initGame(frame);
		new LwjglApplication(frame, config);
	}
}
