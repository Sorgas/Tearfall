package stonering.game.core;

import stonering.game.core.controller.GameController;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;
import stonering.game.core.view.GameView;
import stonering.generators.localgen.LocalGenContainer;

/**
 * Created by Alexander on 10.06.2017.
 */
public class GameMvc {
    private GameContainer container;
    private GameView view;
    private GameController controller;

    public GameMvc(LocalGenContainer container) {
        this.container = new GameContainer(container);
        controller = new GameController(this.container);
        view = new GameView(this.container, controller);
    }

    public GameContainer getContainer() {
        return container;
    }

    public void setContainer(GameContainer container) {
        this.container = container;
    }

    public GameView getView() {
        return view;
    }

    public void setView(GameView view) {
        this.view = view;
    }
}
