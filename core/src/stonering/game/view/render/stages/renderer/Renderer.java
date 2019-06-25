package stonering.game.view.render.stages.renderer;


/**
 * Provides utility methods for rendering scene.
 *
 * @author Alexander on 06.02.2019.
 */
public abstract class Renderer {
    protected DrawingUtil util;

    public Renderer(DrawingUtil util) {
        this.util = util;
    }

    public abstract void render();
}
