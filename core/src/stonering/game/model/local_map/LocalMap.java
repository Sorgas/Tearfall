package stonering.game.model.local_map;

import com.badlogic.gdx.math.Vector2;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.model.local_map.passage.PassageMap;
import stonering.game.model.system.ModelComponent;
import stonering.game.model.tilemaps.LocalTileMapUpdater;
import stonering.game.model.util.UtilByteArray;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;
import stonering.util.global.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;

import static stonering.enums.blocks.PassageEnum.IMPASSABLE;
import static stonering.enums.blocks.PassageEnum.PASSABLE;

/**
 * Contains blocks, and physical parameters, and proxies to entity.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class LocalMap implements ModelComponent, Initable {
    public final BlockTypeMap blockType;
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
        blockType = new BlockTypeMap(xSize, ySize, zSize);
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

    public void normalizePosition(Position position) {
        normalizeRectangle(position, 1, 1);
    }

    public void normalizeRectangle(Position position, int width, int height) {
        position.x = Math.min(Math.max(0, position.x), xSize - width);
        position.y = Math.min(Math.max(0, position.y), ySize - height);
        position.z = Math.min(Math.max(0, position.z), zSize - 1);
    }

    public boolean isWalkPassable(Position pos) {
        return isWalkPassable(pos.x, pos.y, pos.z);
    }

    public boolean isWalkPassable(int x, int y, int z) {
        //TODO reuse
        return passageMap.getPassage(x, y, z) == PASSABLE.VALUE;
    }

    public boolean isFlyPassable(Position pos) {
        return isFlyPassable(pos.x, pos.y, pos.z);
    }

    public boolean isFlyPassable(int x, int y, int z) {
        //TODO
        return inMap(x, y, z) && blockType.getEnumValue(x, y, z).PASSING != IMPASSABLE;
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

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        init();
    }

    public void updateTile(Position position) {
        updatePassage(position);
        if (localTileMapUpdater != null) localTileMapUpdater.updateTile(position.x, position.y, position.z);
    }

    public void updatePassage(Position position) {
        if (passageMap != null) passageMap.updater.update(position.x, position.y, position.z);
    }
}