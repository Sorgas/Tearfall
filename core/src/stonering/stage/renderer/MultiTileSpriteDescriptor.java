package stonering.stage.renderer;

import java.util.Objects;

/**
 * Stores sprite width and height in tiles.
 *
 * @author Alexander on 05.03.2020.
 */
public class MultiTileSpriteDescriptor extends TileSpriteDescriptor{
    public int width;
    public int height;

    public MultiTileSpriteDescriptor(int x, int y, int width, int heigth, int color, boolean topping) {
        super(x, y, color, topping);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MultiTileSpriteDescriptor that = (MultiTileSpriteDescriptor) o;
        return width == that.width &&
                height == that.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), width, height);
    }
}
