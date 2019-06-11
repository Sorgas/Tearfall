package stonering.game.view.tilemaps;

import com.badlogic.gdx.graphics.Color;
import stonering.game.model.ModelComponent;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.global.IntTriple;

import java.util.HashMap;
import java.util.Map;

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
    private transient Map<Position, IntTriple> map;
    private Position cachePosition;

    public LocalTileMap(LocalMap map) {
        this.map = new HashMap<>();
        cachePosition = new Position();
        atlasX = new byte[map.xSize][map.ySize][map.zSize];
        atlasY = new byte[map.xSize][map.ySize][map.zSize];
        atlasNum = new byte[map.xSize][map.ySize][map.zSize];
        for (int x = 0; x < map.xSize; x++) {
            for (int y = 0; y < map.ySize; y++) {
                for (int z = 0; z < map.zSize; z++) {
                    atlasX[x][y][z] = 0;
                    atlasY[x][y][z] = 0;
                    atlasNum[x][y][z] = -1;
                }
            }
        }
    }

    public void setTile(int x, int y, int z, int atlasX, int atlasY, int atlasNum, Color color) {
        this.atlasX[x][y][z] = (byte) atlasX;
        this.atlasY[x][y][z] = (byte) atlasY;
        this.atlasNum[x][y][z] = (byte) atlasNum;
    }

    public void setTile(Position pos, int atlasX, int atlasY, int atlasNum, Color color) {
        setTile(pos.getX(), pos.getY(), pos.getZ(), atlasX, atlasY, atlasNum, color);
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

    public IntTriple getMappedRamp(Position position) {
        return map.get(position);
    }

    public IntTriple getMappedRamp(int x, int y, int z) {
        return map.get(cachePosition.set(x, y, z));
    }
}