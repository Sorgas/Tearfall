package stonering.game.view.tilemaps;

import com.badlogic.gdx.graphics.Color;
import stonering.game.model.ModelComponent;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Contains resolved sprite data of blocks as they are updated rarely.
 * Ramps are main reason, because their resolving require observation of neighbour tiles.
 * //TODO rewrite to use hashmaps only with ramps.
 *
 * @author Alexander Kuzyakov on 02.08.2017.
 */
public class LocalTileMap implements ModelComponent {
    private transient byte[][][] atlasX;
    private transient byte[][][] atlasY;
    private transient byte[][][] atlasNum;
    private transient int xSize;
    private transient int ySize;
    private transient int zSize;

    public LocalTileMap(LocalMap localMap) {
        int xSize = localMap.xSize;
        int ySize = localMap.ySize;
        int zSize = localMap.zSize;
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


    public void setTile(Position pos, int atlasX, int atlasY, int atlasNum, Color color) {
        setTile(pos.getX(), pos.getY(), pos.getZ(), atlasX, atlasY, atlasNum, color);
    }

    public void setTile(int x, int y, int z, int atlasX, int atlasY, int atlasNum, Color color) {
        this.atlasX[x][y][z] = (byte) atlasX;
        this.atlasY[x][y][z] = (byte) atlasY;
        this.atlasNum[x][y][z] = (byte) atlasNum;
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