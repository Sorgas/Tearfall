package stonering.game.core;

import stonering.game.core.controller.GameController;
import stonering.game.core.model.GameContainer;
import stonering.game.core.view.GameView;
import stonering.generators.localgen.LocalGenContainer;

/**
 * Created by Alexander on 10.06.2017.
 */
public class GameMvc {
    private GameContainer model;
    private GameView view;
    private GameController controller;

    public GameMvc(LocalGenContainer container) {
        model = new GameContainer(container);
        controller = new GameController(this.model);
        view = new GameView(this.model, controller);
    }

    public GameContainer getModel() {
        return model;
    }

    public void setModel(GameContainer model) {
        this.model = model;
    }

    public GameView getView() {
        return view;
    }

    public void setView(GameView view) {
        this.view = view;
    }
}
