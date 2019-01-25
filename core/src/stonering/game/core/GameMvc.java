package stonering.game.core;

import stonering.game.core.controller.controllers.GameController;
import stonering.game.core.model.GameContainer;
import stonering.game.core.view.GameView;
import stonering.generators.localgen.LocalGenContainer;

/**
 * Main game singleton MVC.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class GameMvc {
    private static GameMvc instance;
    private GameContainer model;
    private GameView view;
    private GameController controller;

    public static GameMvc createInstance(LocalGenContainer container) {
        instance = new GameMvc(container);
        instance.init();
        return instance;
    }

    private GameMvc(LocalGenContainer container) {
        model = new GameContainer(container); //independent from CV
    }

    public void init() {
        view = new GameView();
        controller = new GameController();
        view.init();
        controller.init();
    }

    public static GameMvc getInstance() {
        return instance;
    }

    public GameContainer getModel() {
        return model;
    }

    public GameView getView() {
        return view;
    }

    public GameController getController() {
        return controller;
    }
}
