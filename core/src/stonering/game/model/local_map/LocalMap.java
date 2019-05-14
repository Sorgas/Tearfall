package stonering.game.model.local_map;

import com.badlogic.gdx.math.Vector2;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.model.ModelComponent;
import stonering.game.model.util.UtilByteArray;
import stonering.game.view.tilemaps.LocalTileMapUpdater;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;
import stonering.util.global.LastInitable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains blocks, and physical parameters, and proxies to entity.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class LocalMap implements ModelComponent, Initable, LastInitable {
    private int[][][] material;
    private byte[][][] blockType;
    private byte[][][] flooding;
    private byte[][][] temperature;
    public final UtilByteArray generalLight;                   //for light from celestial bodies
    public final UtilByteArray light;                          //for light from dynamic sources (torches, lamps)

    public transient PassageMap passage;                             // not saved to savegame,
    private transient LocalTileMapUpdater localTileMapUpdater;           // not saved to savegame,

    public final int xSize;
    public final int ySize;
    public final int zSize;

    public LocalMap(int xSize, int ySize, int zSize) {
        material = new int[xSize][ySize][zSize];
        blockType = new byte[xSize][ySize][zSize];
        flooding = new byte[xSize][ySize][zSize];
        temperature = new byte[xSize][ySize][zSize];
        generalLight = new UtilByteArray(xSize, ySize, zSize);
        light = new UtilByteArray(xSize, ySize, zSize);
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }

    public void init() {
        passage = new AreaInitializer(this).formPassageMap();
        localTileMapUpdater = new LocalTileMapUpdater();
        localTileMapUpdater.flushLocalMap();
    }

    /**
     * Updates material and type of given block.
     * Is called from localgen, digging.
     */
    public void setBlock(int x, int y, int z, byte type, int materialId) {
        blockType[x][y][z] = type;
        material[x][y][z] = materialId;
        if (localTileMapUpdater != null)
            localTileMapUpdater.updateTile(x, y, z);
        if (passage != null) {
            passage.updateCell(x, y, z);
        }
    }

    public void updateBlock(int x, int y, int z) {
        if (localTileMapUpdater != null)
            localTileMapUpdater.updateTile(x, y, z);
    }

    public boolean isWorkingRamp(int x, int y, int z) {
        return blockType[x][y][z] == BlockTypesEnum.RAMP.CODE
                && blockType[x][y][z + 1] == BlockTypesEnum.SPACE.CODE;
    }

    public boolean isWorkingStair(int x, int y, int z) {
        return blockType[x][y][z] == BlockTypesEnum.STAIRS.CODE
                && (blockType[x][y][z + 1] == BlockTypesEnum.STAIRS.CODE
                || blockType[x][y][z + 1] == BlockTypesEnum.STAIRFLOOR.CODE);
    }

    public List<Position> getFreeBlockNear(Position position) {
        List<Position> positions = new ArrayList<>();
        for (int x = position.x - 1; x < position.x + 2; x++) {
            for (int y = position.y - 1; y < position.y + 2; y++) {
                if (x == position.x && y == position.y) continue;
                if (inMap(x, y, position.z) && isWalkPassable(x, y, position.z)) {
                    positions.add(new Position(x, y, position.z));
                }
            }
        }
        return positions;
    }

    public boolean inMap(int x, int y, int z) {
        return !(x < 0 || y < 0 || z < 0 || x >= xSize || y >= ySize || z >= zSize);
    }

    public boolean inMap(Position position) {
        return inMap(position.x, position.y, position.z);
    }

    public boolean inMap(Vector2 vector) {
        return inMap(Math.round(vector.x), Math.round(vector.y), 0);
    }

    public boolean isBorder(int x, int y) {
        return x == 0 || y == 0 || x == xSize - 1 || y == ySize - 1;
    }

    public boolean isBorder(Position position) {
        return isBorder(position.x, position.y);
    }

    public Position normalizePosition(Position position) {
        position.setX(Math.min(Math.max(0, position.x), xSize - 1));
        position.setY(Math.min(Math.max(0, position.y), ySize - 1));
        position.setZ(Math.min(Math.max(0, position.z), zSize - 1));
        return position;
    }

    public void setBlockType(int x, int y, int z, byte type) {
        //TODO update passage
        blockType[x][y][z] = type;
        if (localTileMapUpdater != null)
            localTileMapUpdater.updateTile(x, y, z);
    }

    public boolean isWalkPassable(Position pos) {
        return isWalkPassable(pos.x, pos.y, pos.z);
    }

    public boolean isWalkPassable(int x, int y, int z) {
        //TODO reuse
        return passage.getPassage(x,y,z) == 1;
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        init();
    }

    public boolean isFlyPassable(Position pos) {
        return isFlyPassable(pos.x, pos.y, pos.z);
    }

    public boolean isFlyPassable(int x, int y, int z) {
        //TODO
        return inMap(x, y, z) && BlockTypesEnum.getType(getBlockType(x, y, z)).PASSING != 0; // 1 || 2
    }

    /**
     * Only for adjacent cells.
     */
    public boolean hasPathBetween(Position pos1, Position pos2) {
        return hasPathBetween(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
    }

    /**
     * Only for adjacent cells.
     */
    public boolean hasPathBetween(int x1, int y1, int z1, int x2, int y2, int z2) {
        return passage.hasPathBetween(x1,y1,z1,x2,y2,z2);
    }

    public void setLocalTileMapUpdater(LocalTileMapUpdater localTileMapUpdater) {
        this.localTileMapUpdater = localTileMapUpdater;
    }

    public byte getTemperature(int x, int y, int z) {
        return temperature[x][y][z];
    }

    public int getMaterial(Position pos) {
        return material[pos.x][pos.y][pos.z];
    }

    public int getMaterial(int x, int y, int z) {
        return material[x][y][z];
    }

    public byte getBlockType(Position pos) {
        return getBlockType(pos.x, pos.y, pos.z);
    }

    public byte getBlockType(int x, int y, int z) {
        return inMap(x, y, z) ? blockType[x][y][z] : 0;
    }

    public void setBlockType(Position pos, byte type) {
        setBlockType(pos.x, pos.y, pos.z, type);
    }

    public byte getFlooding(int x, int y, int z) {
        return flooding[x][y][z];
    }

    public byte getFlooding(Position position) {
        return getFlooding(position.x, position.y, position.z);
    }

    public void setFlooding(int x, int y, int z, int value) {
        flooding[x][y][z] = (byte) value;
    }

    public void setFlooding(Position position, int value) {
        setFlooding(position.x, position.y, position.z, value);
    }

    public void setBlock(Position pos, BlockTypesEnum blockType, int materialId) {
        setBlock(pos.x, pos.y, pos.z, blockType.CODE, materialId);
    }

    public void setBlock(Position pos, byte blockType, int materialId) {
        setBlock(pos.x, pos.y, pos.z, blockType, materialId);
    }

    public void setBlock(int x, int y, int z, BlockTypesEnum blockType, int materialId) {
        setBlock(x, y, z, blockType.CODE, materialId);
    }

    public UtilByteArray getGeneralLight() {
        return generalLight;
    }

    public UtilByteArray getLight() {
        return light;
    }

    public PassageMap getPassage() {
        return passage;
    }
}