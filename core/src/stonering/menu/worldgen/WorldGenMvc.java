package stonering.menu.worldgen;

import stonering.TearFall;

/**
 * Created by Alexander on 08.03.2017.
 */
public class WorldGenMvc {
	private WorldGenModel model;
	private WorldGenView view;
	private WorldGenController controller;

	public WorldGenMvc(TearFall game) {
		this.model = new WorldGenModel(game);
		this.view = new WorldGenView(game);
		this.controller = new WorldGenController(game);
		linkComponents();
	}

	private void linkComponents() {
		controller.setModel(model);
		model.setView(view);
		view.setModel(model);
		view.setController(controller);
	}

	public WorldGenView getView() {
		return view;
	}

	public WorldGenModel getModel() {
		return model;
	}

	public WorldGenController getController() {
		return controller;
	}
}