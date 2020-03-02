package stonering.util.geometry;

import java.util.function.Consumer;

/**
 * Represents 3d box.
 */
public class Int3dBounds extends Int2dBounds {
    public int minZ;
    public int maxZ;
    public Consumer<Consumer<Position>> iterator;

    public Int3dBounds(Position pos1, Position pos2) {
        set(pos1, pos2);
    }

    public Int3dBounds() {
        set(0, 0, 0, 0, 0, 0);
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
        iterator = consumer -> {
            for (int x = minX; x <= maxX; x++) {
                for (int y = maxY; y >= minY; y--) {
                    for (int z = minZ; z <= maxZ; z++) {
                        consumer.accept(new Position(x, y, z));
                    }
                }
            }
        };
    }

    public void set(Position pos1, Position pos2) {
        set(Math.min(pos1.x, pos2.x),
                Math.min(pos1.y, pos2.y),
                Math.min(pos1.z, pos2.z),
                Math.max(pos1.x, pos2.x),
                Math.max(pos1.y, pos2.y),
                Math.max(pos1.z, pos2.z));
    }

    public void clamp(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        super.clamp(minX, minY, maxX, maxY);
        this.minZ = Math.max(this.minZ, minZ);
        this.maxZ = Math.min(this.maxZ, maxZ);
    }

    @Override
    public String toString() {
        return "Int3dBounds{" + " " + minX + " " + minY + " " + minZ + " " + maxX + " " + maxY + " " + maxZ + '}';
    }
}
