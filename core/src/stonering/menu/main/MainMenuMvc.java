package stonering.menu.main;

import stonering.TearFall;
import stonering.menu.mvc_interfaces.MvcContainer;

/**
 * Created by Alexander on 02.04.2017.
 */
public class MainMenuMvc {
	private MainMenuModel model;
	private MainMenuView view;
	private MainMenuController controller;

	public MainMenuMvc(TearFall game) {
		model = new MainMenuModel(game);
		view = new MainMenuView();
		controller = new MainMenuController();
		linkComponents();
	}

	private void linkComponents() {
		controller.setModel(model);
		model.setView(view);
		view.setModel(model);
	}

	public MainMenuController getController() {
		return controller;
	}

	public MainMenuView getView() {
		return view;
	}

	public MainMenuModel getModel() {
		return model;
	}
}
