package stonering.game.view.render.util;

import stonering.util.geometry.Position;

/**
 * Represents 3d box.
 */
public class Float3DBounds {
    private float minX;
    private float minY;
    private float minZ;
    private float maxX;
    private float maxY;
    private float maxZ;

    public Float3DBounds() {
        this(0, 0, 0, 0, 0, 0);
    }

    public Float3DBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        set(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public boolean isIn(Position position) {
        return position.x >= minX &&
                position.x <= maxX &&
                position.y >= minY &&
                position.y <= maxY &&
                position.z >= minZ &&
                position.z <= maxZ;
    }

    public void set(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public float getMinX() {
        return minX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMinZ() {
        return minZ;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public float getMaxZ() {
        return maxZ;
    }
}
