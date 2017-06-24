package stonering.game.core;

import stonering.game.core.view.GameView;

/**
 * Created by Alexander on 10.06.2017.
 */
public class GameMvc {
    private GameContainer container;
    private GameView view;

    public GameMvc(LocalMap localMap) {
        container = new GameContainer(localMap);
        view = new GameView();
        container.setMap(localMap);
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
