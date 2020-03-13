package stonering.util.geometry;

import java.util.Objects;

/**
 * @author Alexander on 11.03.2020.
 */
public class IntVector2 {
    public int x, y;

    public IntVector2(int x, int y) {
        set(x, y);
    }

    public IntVector2() {
        this(0, 0);
    }

    public IntVector2(IntVector2 source) {
        this(source.x, source.y);
    }

    public IntVector2(int[] source) {
        this(source[0], source[1]);
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(IntVector2 vector) {
        set(vector.x, vector.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntVector2 that = (IntVector2) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
