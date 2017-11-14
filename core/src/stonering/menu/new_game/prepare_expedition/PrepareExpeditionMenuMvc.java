package stonering.menu.new_game.prepare_expedition;

import stonering.TearFall;
import stonering.menu.mvc_interfaces.MvcContainer;

/**
 * Created by Alexander on 14.04.2017.
 */
public class PrepareExpeditionMenuMvc implements MvcContainer {
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

	@Override
	public PrepareExpeditionMenuController getController() {
		return controller;
	}

	@Override
	public PrepareExpeditionMenuView getView() {
		return view;
	}

	@Override
	public PrepareExpeditionMenuModel getModel() {
		return model;
	}
}
