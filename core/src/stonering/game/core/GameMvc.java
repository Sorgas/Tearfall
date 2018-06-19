package stonering.game.core;

import stonering.game.core.controller.controllers.GameController;
import stonering.game.core.model.GameContainer;
import stonering.game.core.view.GameView;
import stonering.generators.localgen.LocalGenContainer;

/**
 * Main game MVC.
 * <p>
 * Created by Alexander on 10.06.2017.
 */
public class GameMvc {
    private GameContainer model;
    private GameView view;
    private GameController controller;

    public GameMvc(LocalGenContainer container) {
        model = new GameContainer(container); //independent from CV
        view = new GameView(this);
        controller = new GameController(this);

        view.init();
        controller.init();
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
