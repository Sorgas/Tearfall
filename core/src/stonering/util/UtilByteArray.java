package stonering.util;

import stonering.util.geometry.Position;

import java.io.Serializable;

/**
 * Byte Array with util methods.
 *
 * @author Alexander Kuzyakov
 */
public class UtilByteArray implements Serializable {
    protected byte[][][] array;
    Position size;

    public UtilByteArray(int xSize, int ySize, int zSize) {
        size = new Position(xSize, ySize, zSize);
        array = new byte[xSize][ySize][zSize];
    }

    public byte get(int x, int y, int z) {
        return array[x][y][z];
    }

    public byte get(Position position) {
        return get(position.x, position.y, position.z);
    }

    public void set(int x, int y, int z, int value) {
        array[x][y][z] = (byte) value;
    }

    public void set(Position position, int value) {
        set(position.x, position.y, position.z, value);
    }

    public void change(int x, int y, int z, byte delta) {
        array[x][y][z] += delta;
    }

    public boolean withinBounds(int x, int y, int z) {
        return x >= 0 && y >= 0 && z >= 0 && x < size.x && y < size.y && z < size.z;
    }
}
