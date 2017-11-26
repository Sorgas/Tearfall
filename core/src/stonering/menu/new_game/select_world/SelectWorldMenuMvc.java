package stonering.menu.new_game.select_world;

import stonering.TearFall;

/**
 * Created by Alexander on 14.04.2017.
 */
public class SelectWorldMenuMvc {
	private SelectWorldMenuModel model;
	private SelectWorldMenuView view;
	private SelectWorldMenuController controller;

	public SelectWorldMenuMvc(TearFall game) {
		model = new SelectWorldMenuModel(game);
		view = new SelectWorldMenuView(game);
		controller = new SelectWorldMenuController(game);
		linkComponents();
	}

	private void linkComponents() {
		controller.setModel(model);
		model.setView(view);
		view.setModel(model);
	}

	public SelectWorldMenuController getController() {
		return controller;
	}

	public SelectWorldMenuView getView() {
		return view;
	}

	public SelectWorldMenuModel getModel() {
		return model;
	}
}
