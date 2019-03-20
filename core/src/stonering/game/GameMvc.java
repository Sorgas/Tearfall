package stonering.game;

import stonering.game.controller.controllers.GameController;
import stonering.game.model.GameModel;
import stonering.game.view.GameView;

import java.io.Serializable;

/**
 * Main game singleton MVC.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class GameMvc implements Serializable {
    private static GameMvc instance;
    private GameModel model;
    private GameView view;
    private GameController controller;

    public static GameMvc createInstance(GameModel gameModel) {
        instance = new GameMvc(gameModel);
        return instance;
    }

    private GameMvc(GameModel gameModel) {
        model = gameModel;
        view = new GameView();
        controller = new GameController();
    }

    public void init() {
        model.init();
        controller.init();
        view.init();
    }

    public static GameMvc getInstance() {
        return instance;
    }

    public GameModel getModel() {
        return model;
    }

    public GameView getView() {
        return view;
    }

    public GameController getController() {
        return controller;
    }
}
