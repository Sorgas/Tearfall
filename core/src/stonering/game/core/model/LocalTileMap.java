package stonering.game.core.model;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Alexander on 02.08.2017.
 */
public class LocalTileMap {
    private byte[][][] atlasX;
    private byte[][][] atlasY;
    private Color[][][] color;

    private int xSize;
    private int ySize;
    private int zSize;

    public LocalTileMap(int xSize, int ySize, int zSize) {
        atlasX = new byte[xSize][ySize][zSize];
        atlasY = new byte[xSize][ySize][zSize];
        color = new Color[xSize][ySize][zSize];
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

    public Color getColor(int x, int y, int z) {
        return color[x][y][z];
    }

    public void setTile(int x, int y, int z, byte atlasX, byte atlasY, Color color) {
        this.atlasX[x][y][z] = atlasX;
        this.atlasY[x][y][z] = atlasY;
        this.color[x][y][z] = color;
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
