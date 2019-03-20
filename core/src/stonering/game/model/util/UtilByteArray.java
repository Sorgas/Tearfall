package stonering.game.model.util;

import stonering.util.geometry.Position;

import java.io.Serializable;

/**
 * Byte Array with util methods.
 *
 * @author Alexander Kuzyakov
 */
public class UtilByteArray implements Serializable {
    private byte[][][] array;

    public UtilByteArray(int xSize, int ySize, int zSize) {
        array = new byte[xSize][ySize][zSize];
    }

    public byte getValue(int x, int y, int z) {
        return array[x][y][z];
    }

    public void setValue(int x, int y, int z, int value) {
        this.array[x][y][z] = (byte) value;
    }

    public void changeValue(int x, int y, int z, byte delta) {
        array[x][y][z] += delta;
    }

    public byte getValue(Position position) {
        return array[position.x][position.y][position.z];
    }

    public void setValue(Position position, byte value) {
        this.array[position.x][position.y][position.z] = value;
    }

    public void changeValue(Position position, byte delta) {
        array[position.x][position.y][position.z] += delta;
    }
}
