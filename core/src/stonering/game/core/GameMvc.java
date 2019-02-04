package stonering.game.core;

import stonering.game.core.controller.controllers.GameController;
import stonering.game.core.model.MainGameModel;
import stonering.game.core.view.GameView;
import stonering.generators.localgen.LocalGenContainer;

/**
 * Main game singleton MVC.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class GameMvc {
    private static GameMvc instance;
    private MainGameModel model;
    private GameView view;
    private GameController controller;

    public static GameMvc createInstance(LocalGenContainer container) {
        instance = new GameMvc();
        instance.init(container);
        return instance;
    }

    private GameMvc() {
        model = new MainGameModel();
        view = new GameView();
        controller = new GameController();
    }

    public void init(LocalGenContainer container) {
        model.loadFromContainer(container);
        model.init();
        controller.init();
        view.init();
    }

    public static GameMvc getInstance() {
        return instance;
    }

    public MainGameModel getModel() {
        return model;
    }

    public GameView getView() {
        return view;
    }

    public GameController getController() {
        return controller;
    }
}
