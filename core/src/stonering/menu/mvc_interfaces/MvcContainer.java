package stonering.menu.mvc_interfaces;

/**
 * Created by Alexander on 08.03.2017.
 */
public interface MvcContainer {
	GameController getController();
	GameView getView();
	GameModel getModel();
}