package stonering.stage.renderer;

import java.util.Objects;

/**
 * Describes tile, taken from texture. Used as key for caching sprites.
 *
 * @author Alexander on 06.10.2019.
 */
public class TileKey {
    public int x;
    public int y;
    public int width;
    public int height;
    public int color;

    public TileKey(int x, int y, int width, int height, int color) {
        set(x, y, width, height, color);
    }

    public TileKey(int x, int y, int color) {
        set(x, y, 1, 1, color);
    }

    public void set(int x, int y, int width, int height, int color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TileKey that = (TileKey) o;
        return x == that.x &&
                y == that.y &&
                width == that.width &&
                height == that.height &&
                color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, width, height, color);
    }
}
