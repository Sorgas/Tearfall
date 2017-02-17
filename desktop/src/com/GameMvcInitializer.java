package com;

import com.frames.GameFrame;
import com.model.FlatworldModel;
import com.model.GameModel;
import com.view.GameView;
import com.view.SimpleView;

public class GameMvcInitializer {

	public void initGame(GameFrame frame) {
		GameModel model = new FlatworldModel();
		GameView view = new SimpleView();
		view.setModel(model);
		frame.setView(view);
	}
}
