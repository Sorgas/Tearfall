package stonering.stage.renderer;

/**
 * Used as key for caching sprites.
 *
 * @author Alexander on 06.10.2019.
 */
public class SpriteDescriptor {
    public final int x;
    public final int y;
    public final int color;
    public final boolean topping;

    public SpriteDescriptor(int x, int y, int color, boolean topping) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.topping = topping;
    }
}
