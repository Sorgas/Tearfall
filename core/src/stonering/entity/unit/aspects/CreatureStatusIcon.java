package stonering.entity.unit.aspects;

import java.util.Objects;

/**
 * Stores icon coordinates to be drawn upon creature sprite.
 *
 * @author Alexander on 05.10.2019.
 */
public class CreatureStatusIcon {
    public final int x;
    public final int y;

    public CreatureStatusIcon(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public CreatureStatusIcon(int[] atlasXY) {
        x = atlasXY[0];
        y = atlasXY[1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreatureStatusIcon that = (CreatureStatusIcon) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
