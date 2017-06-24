package stonering.menu.main;

import stonering.game.mvc_interfaces.GameController;

/**
 * Created by Alexander on 02.04.2017.
 */
public class MainMenuController implements GameController {
	private MainMenuModel model;

	public void setModel(MainMenuModel model) {
		this.model = model;
	}
}
