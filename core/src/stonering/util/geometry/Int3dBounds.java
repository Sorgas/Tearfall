package stonering.util.geometry;

/**
 * Represents 3d box.
 */
public class Int3dBounds extends Int2dBounds {
    private int minZ;
    private int maxZ;

    public Int3dBounds() {
        this(0, 0, 0, 0, 0, 0);
    }

    public Int3dBounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        set(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public boolean isIn(Position position) {
        return super.isIn(position) && position.z <= maxZ && position.z >= minZ;
    }

    public void set(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        super.set(minX, minY, maxX, maxY);
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    public void clamp(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        super.clamp(minX, minY, maxX, maxY);
        this.minZ = Math.max(this.minZ, minZ);
        this.maxZ = Math.min(this.maxZ, maxZ);
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxZ() {
        return maxZ;
    }

    @Override
    public String toString() {
        return "Int3dBounds{" + " " + minX + " " + minY + " " + minZ + " " + maxX + " " + maxY + " " + maxZ + '}';
    }
}
