package stonering.game;

import stonering.game.controller.controllers.GameController;
import stonering.game.model.GameModel;
import stonering.screen.GameView;
import stonering.util.global.Logger;

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
        new GameMvc(gameModel);
        return instance;
    }

    private GameMvc(GameModel gameModel) {
        instance = this;
        model = gameModel;
    }

    public void createViewAndController() {
        view = new GameView();
        controller = new GameController();
    }

    public void init() {
        if(model != null) model.init();
        else Logger.GENERAL.logWarn("Attempt to init MVC with no model.");
        if(controller != null) controller.init();
    }

    public static GameMvc instance() {
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
