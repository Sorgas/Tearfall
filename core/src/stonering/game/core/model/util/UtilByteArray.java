package stonering.game.core.model.util;

import stonering.util.geometry.Position;

/**
 * Byte Array with util methods.
 *
 * @author Alexander Kuzyakov
 */
public class UtilByteArray {
    private byte[][][] array;
    private int xSize;
    private int ySize;
    private int zSize;

    public UtilByteArray(int xSize, int ySize, int zSize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
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
        return array[position.getX()][position.getY()][position.getZ()];
    }

    public void setValue(Position position, byte value) {
        this.array[position.getX()][position.getY()][position.getZ()] = value;
    }

    public void changeValue(Position position, byte delta) {
        array[position.getX()][position.getY()][position.getZ()] += delta;
    }
}
