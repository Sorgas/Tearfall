package stonering.game.view.render.stages.base;


/**
 * Provides utility methods for rendering scene.
 *
 * @author Alexander on 06.02.2019.
 */
public abstract class Renderer {
    protected DrawingUtil drawingUtil;

    public Renderer(DrawingUtil drawingUtil) {
        this.drawingUtil = drawingUtil;
    }

    public abstract void render();
}
