package stonering.stage.renderer;


/**
 * Provides utility methods for rendering scene.
 *
 * @author Alexander on 06.02.2019.
 */
public abstract class Renderer {
    protected SpriteDrawingUtil util;

    public Renderer(SpriteDrawingUtil util) {
        this.util = util;
    }

    public abstract void render();
}
