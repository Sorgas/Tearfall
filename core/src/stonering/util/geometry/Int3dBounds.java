package stonering.util.geometry;

import java.util.function.Consumer;

import stonering.util.lang.TriConsumer;

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

    public Int3dBounds(Int3dBounds source) {
        this(source.minX, source.minY, source.minZ, source.maxX, source.maxY, source.maxZ);
    }

    public Int3dBounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        set(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public boolean isIn(Position position) {
        return isIn(position.x, position.y, position.z);
    }

    public boolean isIn(int x, int y, int z) {
        return super.isIn(x, y) && z <= maxZ && z >= minZ;
    }
    
    public void set(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        super.set(minX, minY, maxX, maxY);
        this.minZ = Math.min(minZ, maxZ);
        this.maxZ = Math.max(minZ, maxZ);
    }

    public void set(Position pos1, Position pos2) {
        set(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
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
        extendTo(position.x, position.y, position.z);
    }

    public void extendTo(int x, int y, int z) {
        maxX = Math.max(maxX, x);
        minX = Math.min(minX, x);
        maxY = Math.max(maxY, y);
        minY = Math.min(minY, y);
        maxZ = Math.max(maxZ, z);
        minZ = Math.min(minZ, z);
    }

    public void iterate(Consumer<Position> consumer) {
        iterate((x, y, z) -> consumer.accept(new Position(x, y, z)));
    }

    public void iterate(TriConsumer<Integer, Integer, Integer> consumer) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = maxY; y >= minY; y--) {
                for (int z = minZ; z <= maxZ; z++) {
                    consumer.accept(x, y, z);
                }
            }
        }
    }

    @Override
    public Int3dBounds clone() {
        return new Int3dBounds(this);
    }

    @Override
    public String toString() {
        return "Int3dBounds{" + " " + minX + " " + minY + " " + minZ + " " + maxX + " " + maxY + " " + maxZ + '}';
    }
}
