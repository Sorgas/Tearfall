package stonering.stage.renderer;

/**
 * Contains drawing utils.
 *
 * @author Alexander on 06.02.2019.
 */
public abstract class Drawer {
    protected SpriteDrawingUtil spriteUtil;
    protected ShapeDrawingUtil shapeUtil;

    public Drawer(SpriteDrawingUtil spriteUtil, ShapeDrawingUtil shapeUtil) {
        this.spriteUtil = spriteUtil;
        this.shapeUtil = shapeUtil;
    }
}
