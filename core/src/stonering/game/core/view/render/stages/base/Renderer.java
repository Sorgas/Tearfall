package stonering.game.core.view.render.stages.base;

import stonering.game.core.model.GameModel;

/**
 * Provides utility methods for rendering scene.
 *
 * @author Alexander on 06.02.2019.
 */
public abstract class Renderer {
    protected GameModel gameModel;
    protected DrawingUtil drawingUtil;

    public Renderer(GameModel gameModel, DrawingUtil drawingUtil) {
        this.gameModel = gameModel;
        this.drawingUtil = drawingUtil;
    }

    public abstract void render();
}
