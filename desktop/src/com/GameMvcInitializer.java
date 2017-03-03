package com;

import com.frames.GameFrame;
import com.model.Model;
import com.model.GameModel;
import com.view.GameView;
import com.view.WorldView;

public class GameMvcInitializer {

	public void initGame(GameFrame frame) {
		GameModel model = new Model();
		//GameView simpleView = new SimpleView();
		GameView worldView = new WorldView();
		GameView view = worldView;
		view.setModel(model);
		frame.setView(view);
	}
}
