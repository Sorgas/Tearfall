package stonering.util.geometry;

import com.badlogic.gdx.math.Vector2;

/**
 * @author Alexander on 14.06.2019.
 */
public class Float2dBounds {
    protected float minX;
    protected float minY;
    protected float maxX;
    protected float maxY;

    public Float2dBounds() {
        this(0, 0, 0, 0);
    }

    public Float2dBounds(float minX, float minY, float maxX, float maxY) {
        set(minX, minY, maxX, maxY);
    }

    public void set(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public Vector2 getOutVector(Vector2 position) {
        Vector2 vector = new Vector2();
        if (position.x < minX) vector.x = position.x - minX;
        if (position.x > maxX) vector.x = position.x - maxX;
        if (position.y < minY) vector.y = position.y - minY;
        if (position.y > maxY) vector.y = position.y - maxY;
        return vector;
    }

    public float getMinX() {
        return minX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }
}
