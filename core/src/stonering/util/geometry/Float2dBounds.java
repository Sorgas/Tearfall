package stonering.util.geometry;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

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

    public Vector3 getOutVector(Vector2 bottomLeft, Vector2 topRight) {
        Vector3 vector = new Vector3();
        if (bottomLeft.x < minX) vector.x = bottomLeft.x - minX;
        if (topRight.x > maxX) vector.x = topRight.x - maxX;
        if (bottomLeft.y < minY) vector.y = bottomLeft.y - minY;
        if (topRight.y > maxY) vector.y = topRight.y - maxY;
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
