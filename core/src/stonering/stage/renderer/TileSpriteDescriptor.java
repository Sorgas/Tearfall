package stonering.stage.renderer;

import java.util.Objects;

/**
 * Used as key for caching sprites.
 *
 * @author Alexander on 06.10.2019.
 */
public class TileSpriteDescriptor {
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final int color;
    public final boolean topping;

    public TileSpriteDescriptor(int x, int y, int width, int height, int color, boolean topping) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.topping = topping;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TileSpriteDescriptor that = (TileSpriteDescriptor) o;
        return x == that.x &&
                y == that.y &&
                width == that.width &&
                height == that.height &&
                color == that.color &&
                topping == that.topping;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, width, height, color, topping);
    }
}
