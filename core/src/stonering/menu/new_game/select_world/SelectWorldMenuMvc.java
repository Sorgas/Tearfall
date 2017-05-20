package stonering.menu.new_game.select_world;

import stonering.TearFall;
import stonering.menu.MvcContainer;

/**
 * Created by Alexander on 14.04.2017.
 */
public class SelectWorldMenuMvc implements MvcContainer {
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

	@Override
	public SelectWorldMenuController getController() {
		return controller;
	}

	@Override
	public SelectWorldMenuView getView() {
		return view;
	}

	@Override
	public SelectWorldMenuModel getModel() {
		return model;
	}
}
