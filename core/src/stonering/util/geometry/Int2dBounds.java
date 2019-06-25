package stonering.util.geometry;

/**
 * @author Alexander_Kuzyakov on 10.06.2019.
 */
public class Int2dBounds {
    protected int minX;
    protected int minY;
    protected int maxX;
    protected int maxY;

    public Int2dBounds() {
        this(0, 0, 0, 0);
    }

    public Int2dBounds(int minX, int minY, int maxX, int maxY) {
        set(minX, minY, maxX, maxY);
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

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    @Override
    public String toString() {
        return "Int3dBounds{" + " " + minX + " " + minY + " " + maxX + " " + maxY + '}';
    }
}
