package stonering.menu.new_game.select_location;

import stonering.TearFall;

/**
 * Created by Alexander on 14.04.2017.
 */
public class SelectLocationMenuMvc {
	private SelectLocationMenuModel model;
	private SelectLocationMenuView view;
	private SelectLocationMenuController controller;

	public SelectLocationMenuMvc(TearFall game) {
		model = new SelectLocationMenuModel(game);
		view = new SelectLocationMenuView(game);
		controller = new SelectLocationMenuController(game);
		linkComponents();
	}

	private void linkComponents() {
		controller.setModel(model);
		model.setView(view);
		view.setModel(model);
	}

	public SelectLocationMenuController getController() {
		return controller;
	}

	public SelectLocationMenuView getView() {
		return view;
	}

	public SelectLocationMenuModel getModel() {
		return model;
	}
}
