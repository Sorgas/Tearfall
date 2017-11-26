package stonering.menu.new_game.prepare_expedition;

import stonering.TearFall;

/**
 * Created by Alexander on 14.04.2017.
 */
public class PrepareExpeditionMenuMvc {
	private PrepareExpeditionMenuModel model;
	private PrepareExpeditionMenuView view;
	private PrepareExpeditionMenuController controller;

	public PrepareExpeditionMenuMvc(TearFall game) {
		model = new PrepareExpeditionMenuModel(game);
		view = new PrepareExpeditionMenuView(game);
		controller = new PrepareExpeditionMenuController(game);
		linkComponents();
	}

	private void linkComponents() {
		controller.setModel(model);
		model.setView(view);
		view.setModel(model);
	}

	public PrepareExpeditionMenuController getController() {
		return controller;
	}

	public PrepareExpeditionMenuView getView() {
		return view;
	}

	public PrepareExpeditionMenuModel getModel() {
		return model;
	}
}
