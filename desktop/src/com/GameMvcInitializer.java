package com;

import com.MVCcontainer.WorldGenMvc;
import com.frames.GameFrame;

public class GameMvcInitializer {

	public void initGame(GameFrame frame) {
		WorldGenMvc mvcContainer = new WorldGenMvc();
		mvcContainer.getController().generateWorld();
		frame.setMvcContainer(mvcContainer);
	}
}