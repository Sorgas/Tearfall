package stonering.game.model.local_map;

import com.badlogic.gdx.math.Vector2;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.passage.PassageMap;
import stonering.game.model.system.ModelComponent;
import stonering.game.model.system.plant.PlantContainer;
import stonering.game.model.system.substrate.SubstrateContainer;
import stonering.game.model.tilemaps.LocalTileMapUpdater;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;
import stonering.util.global.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import static stonering.enums.blocks.BlockTypeEnum.PassageEnum.IMPASSABLE;
import static stonering.enums.blocks.BlockTypeEnum.PassageEnum.PASSABLE;

/**
 * Contains blocks, and physical parameters, and proxies to entity.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class LocalMap implements ModelComponent, Initable {
    private int[][][] material;
    private byte[][][] blockType;
    private byte[][][] flooding;
    private byte[][][] temperature;
    private Position cachePosition;


    public LightMap light;
    public transient PassageMap passageMap;                                 // not saved to savegame,
    private transient LocalTileMapUpdater localTileMapUpdater;           // not saved to savegame,

    public final int xSize;
    public final int ySize;
    public final int zSize;

    public LocalMap(int xSize, int ySize, int zSize) {
        material = new int[xSize][ySize][zSize];
        blockType = new byte[xSize][ySize][zSize];
        flooding = new byte[xSize][ySize][zSize];
        temperature = new byte[xSize][ySize][zSize];
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        cachePosition = new Position();
        light = new LightMap(this);
    }

    public void init() {
        Logger.LOADING.logDebug("Initing local map");
        light.initLight();
        localTileMapUpdater = new LocalTileMapUpdater();
        localTileMapUpdater.flushLocalMap();
    }

    public void initAreas() {
        passageMap = new PassageMap(this);
        passageMap.init();
    }

    /**
     * Updates material and type of given block.
     * Is called from localgen, digging.
     */
    public void setBlock(int x, int y, int z, byte type, int materialId) {
        material[x][y][z] = materialId;
        setBlockType(x, y, z, type);
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
        position.x = Math.min(Math.max(0, position.x), xSize - 1);
        position.y = Math.min(Math.max(0, position.y), ySize - 1);
        position.z = Math.min(Math.max(0, position.z), zSize - 1);
        return position;
    }

    private void setBlockType(int x, int y, int z, byte type) {
        if (type == BlockTypeEnum.SPACE.CODE) deletePlantsOnDeletedBlock(x, y, z);
        blockType[x][y][z] = type;
        if (passageMap != null) passageMap.updater.update(x, y, z);
        if (localTileMapUpdater != null) localTileMapUpdater.updateTile(x, y, z);
    }

    public void updateTile(int x, int y, int z) {
        if(passageMap != null) passageMap.updater.update(x, y, z);
    }

    public void updateTile(Position position) {
        updateTile(position.x, position.y, position.z);
    }

    private void deletePlantsOnDeletedBlock(int x, int y, int z) {
        GameMvc.instance().model().get(SubstrateContainer.class).remove(cachePosition.set(x, y, z));
        GameMvc.instance().model().get(PlantContainer.class).handleBlockRemoval(cachePosition);
    }

    /**
     * Returns tile adjacent to given and with walk passing.
     * Returns same position if no neighbour found.
     */
    public Position getAnyNeighbourPosition(Position position, BlockTypeEnum.PassageEnum passing) {
        for (int x = position.x - 1; x < position.x + 2; x++) {
            for (int y = position.y - 1; y < position.y + 2; y++) {
                if (inMap(position) && passageMap.passage.get(x, y, position.z) == passing.VALUE)
                    return new Position(x, y, position.z);
            }
        }
        return position;
    }

    public boolean isWalkPassable(Position pos) {
        return isWalkPassable(pos.x, pos.y, pos.z);
    }

    public boolean isWalkPassable(int x, int y, int z) {
        //TODO reuse
        return passageMap.getPassage(x, y, z) == PASSABLE.VALUE;
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
        return inMap(x, y, z) && BlockTypeEnum.getType(getBlockType(x, y, z)).PASSING != IMPASSABLE;
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

    public BlockTypeEnum getBlockTypeEnumValue(Position position) {
        return BlockTypeEnum.getType(getBlockType(position.x, position.y, position.z));
    }

    public BlockTypeEnum getBlockTypeEnumValue(int x, int y, int z) {
        return BlockTypeEnum.getType(getBlockType(x, y, z));
    }

    public byte getBlockType(Position pos) {
        return getBlockType(pos.x, pos.y, pos.z);
    }

    public byte getBlockType(int x, int y, int z) {
        return inMap(x, y, z) ? blockType[x][y][z] : BlockTypeEnum.SPACE.CODE;
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

    public void setBlock(Position pos, BlockTypeEnum blockType, int materialId) {
        setBlock(pos.x, pos.y, pos.z, blockType.CODE, materialId);
    }

    public void setBlock(Position pos, byte blockType, int materialId) {
        setBlock(pos.x, pos.y, pos.z, blockType, materialId);
    }

    public void setBlock(int x, int y, int z, BlockTypeEnum blockType, int materialId) {
        setBlock(x, y, z, blockType.CODE, materialId);
    }
}