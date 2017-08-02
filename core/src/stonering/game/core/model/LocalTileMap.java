package stonering.game.core.model;

/**
 * Created by Alexander on 02.08.2017.
 */
public class LocalTileMap {
    private byte[][][] atlasX;
    private byte[][][] atlasY;
    private byte[][][] colorNum;

    private int xSize;
    private int ySize;
    private int zSize;

    public LocalTileMap(int xSize, int ySize, int zSize) {
        atlasX = new byte[xSize][ySize][zSize];
        atlasY = new byte[xSize][ySize][zSize];
        colorNum = new byte[xSize][ySize][zSize];
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }

    public byte getAtlasX(int x, int y, int z) {
        return atlasX[x][y][z];
    }

    public byte getAtlasY(int x, int y, int z) {
        return atlasY[x][y][z];
    }

    public byte getColorNum(int x, int y, int z) {
        return colorNum[x][y][z];
    }

    public void setTile(int x, int y, int z, byte atlasX, byte atlasY, byte color) {

    }

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

    public int getzSize() {
        return zSize;
    }
}
