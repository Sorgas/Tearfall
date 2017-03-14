package com.MVCcontainer;

import com.controller.GameController;
import com.model.GameModel;
import com.view.GameView;

/**
 * Created by Alexander on 08.03.2017.
 */
public interface MvcContainer {
	GameController getController();
	GameView getView();
	GameModel getModel();
}