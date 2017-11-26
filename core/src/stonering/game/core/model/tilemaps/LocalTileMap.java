package stonering.game.core.model.tilemaps;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Alexander on 02.08.2017.
 */
public class LocalTileMap {
    private int TREE_ATLAS_X_MOD = 20; //blocks, with types more than this, are blocks of trees

    private byte[][][] atlasX;
    private byte[][][] atlasY;
    private byte[][][] atlasNum;
    private int xSize;
    private int ySize;
    private int zSize;

    public LocalTileMap(int xSize, int ySize, int zSize) {
        atlasX = new byte[xSize][ySize][zSize];
        atlasY = new byte[xSize][ySize][zSize];
        atlasNum = new byte[xSize][ySize][zSize];
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                for (int z = 0; z < zSize; z++) {
                    atlasX[x][y][z] = 0;
                    atlasY[x][y][z] = 0;
                    atlasNum[x][y][z] = -1;
                }
            }
        }
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

    public byte getAtlasNum(int x, int y, int z) {
        return atlasNum[x][y][z];
    }

    public void setTile(int x, int y, int z, byte atlasX, byte atlasY, byte atlasNum, Color color) {
        this.atlasX[x][y][z] = atlasX;
        this.atlasY[x][y][z] = atlasY;
        this.atlasNum[x][y][z] = atlasNum;
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

    public int getTREE_ATLAS_X_MOD() {
        return TREE_ATLAS_X_MOD;
    }
}