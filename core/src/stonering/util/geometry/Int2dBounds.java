package stonering.util.geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Alexander_Kuzyakov on 10.06.2019.
 */
public class Int2dBounds {
    public int minX;
    public int minY;
    public int maxX;
    public int maxY;

    public Int2dBounds() {
        this(0, 0, 0, 0);
    }

    public Int2dBounds(int minX, int minY, int maxX, int maxY) {
        set(minX, minY, maxX, maxY);
    }

    public Int2dBounds(Position start, IntVector2 size) {
        this(start, size.x, size.y);
    }

    public Int2dBounds(Position start, int width, int height) {
        this(start.x, start.y, start.x + width - 1, start.y + height - 1);
    }

    public boolean isIn(Position position) {
        return position.x >= minX &&
                position.x <= maxX &&
                position.y >= minY &&
                position.y <= maxY;
    }

    public void set(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public void clamp(int minX, int minY, int maxX, int maxY) {
        this.minX = Math.max(this.minX, minX);
        this.minY = Math.max(this.minY, minY);
        this.maxX = Math.min(this.maxX, maxX);
        this.maxY = Math.min(this.maxY, maxY);
    }

    public void extend(int value) {
        minX -= value;
        minY -= value;
        maxX += value;
        maxY += value;
    }

    public void extendTo(IntVector2 vector) {
        extendTo(vector.x, vector.y);
    }

    public void extendTo(int x, int y) {
        if (x > 0) maxX += x;
        if (x < 0) minX += x;
        if (y > 0) maxY += y;
        if (y < 0) minY += y;
    }

    public Stream<IntVector2> stream() {
        List<IntVector2> list = new ArrayList<>();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                list.add(new IntVector2(x, y));
            }
        }
        return list.stream();
    }

    public List<IntVector2> collectBorders() {
        List<IntVector2> vectors = new ArrayList<>();
        for (int x = minX; x <= maxX; x++) {
            vectors.add(new IntVector2(x, minY));
            vectors.add(new IntVector2(x, maxY));
        }
        for (int y = minY + 1; y < maxY; y++) {
            vectors.add(new IntVector2(minX, y));
            vectors.add(new IntVector2(maxX, y));
        }
        return vectors;
    }

    @Override
    public String toString() {
        return "Int3dBounds{" + " " + minX + " " + minY + " " + maxX + " " + maxY + '}';
    }
}
