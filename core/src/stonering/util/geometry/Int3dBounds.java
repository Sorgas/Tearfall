package stonering.util.geometry;

import java.util.function.Consumer;

/**
 * Represents 3d box.
 */
public class Int3dBounds extends Int2dBounds {
    public int minZ;
    public int maxZ;

    public Int3dBounds(Position pos1, Position pos2) {
        this(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
    }

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

    /**
     * Extends bounds so given position becomes included.
     */
    public void extendTo(Position position) {
        maxX = Math.max(maxX, position.x);
        minX = Math.min(minX, position.x);
        maxY = Math.max(maxY, position.y);
        minY = Math.min(minY, position.y);
        maxZ = Math.max(maxZ, position.z);
        minZ = Math.min(minZ, position.z);
    }

    public void iterate(Consumer<Position> consumer) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = maxY; y >= minY; y--) {
                for (int z = minZ; z <= maxZ; z++) {
                    consumer.accept(new Position(x, y, z));
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return "Int3dBounds{" + " " + minX + " " + minY + " " + minZ + " " + maxX + " " + maxY + " " + maxZ + '}';
    }
}
