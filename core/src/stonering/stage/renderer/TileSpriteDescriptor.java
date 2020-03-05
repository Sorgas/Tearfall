package stonering.stage.renderer;

/**
 * Used as key for caching sprites.
 *
 * @author Alexander on 06.10.2019.
 */
public class TileSpriteDescriptor {
    public final int x;
    public final int y;
    public final int color;
    public final boolean topping;

    public TileSpriteDescriptor(int x, int y, int color, boolean topping) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.topping = topping;
    }
}
