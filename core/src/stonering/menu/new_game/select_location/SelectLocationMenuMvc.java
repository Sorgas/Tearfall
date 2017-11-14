package stonering.menu.new_game.select_location;

import stonering.TearFall;
import stonering.menu.mvc_interfaces.MvcContainer;

/**
 * Created by Alexander on 14.04.2017.
 */
public class SelectLocationMenuMvc implements MvcContainer {
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

	@Override
	public SelectLocationMenuController getController() {
		return controller;
	}

	@Override
	public SelectLocationMenuView getView() {
		return view;
	}

	@Override
	public SelectLocationMenuModel getModel() {
		return model;
	}
}
