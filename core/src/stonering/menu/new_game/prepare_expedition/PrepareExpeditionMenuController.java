package stonering.menu.new_game.prepare_expedition;

import stonering.TearFall;
import stonering.game.mvc_interfaces.GameController;

/**
 * Created by Alexander on 14.04.2017.
 */
public class PrepareExpeditionMenuController implements GameController {
	private PrepareExpeditionMenuModel model;

	public PrepareExpeditionMenuController(TearFall game) {
	}

	public void setModel(PrepareExpeditionMenuModel model) {
		this.model = model;
	}
}
