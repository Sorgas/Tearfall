package stonering.menu.new_game.select_location;

import stonering.TearFall;
import stonering.menu.GameController;

/**
 * Created by Alexander on 14.04.2017.
 */
public class SelectLocationMenuController implements GameController {
	private SelectLocationMenuModel model;

	public SelectLocationMenuController(TearFall game) {
	}

	public void setModel(SelectLocationMenuModel model) {
		this.model = model;
	}
}
