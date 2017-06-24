package stonering.menu.new_game.select_world;

import stonering.TearFall;
import stonering.game.mvc_interfaces.GameController;

/**
 * Created by Alexander on 14.04.2017.
 */
public class SelectWorldMenuController implements GameController {
	private SelectWorldMenuModel model;

	public SelectWorldMenuController(TearFall game) {
	}

	public void setModel(SelectWorldMenuModel model) {
		this.model = model;
	}
}
