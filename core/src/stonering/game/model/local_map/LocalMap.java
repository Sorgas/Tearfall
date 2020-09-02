package stonering.game.model.local_map;

import com.badlogic.gdx.math.Vector2;

import stonering.game.model.local_map.passage.PassageMap;
import stonering.game.model.system.ModelComponent;
import stonering.game.model.tilemaps.LocalTileMapUpdater;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.Position;
import stonering.util.lang.Initable;
import stonering.util.logging.Logger;

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
    public final Int3dBounds bounds;

    public LightMap light;
    public transient PassageMap passageMap;                                 // not saved to savegame,
    private transient LocalTileMapUpdater localTileMapUpdater;           // not saved to savegame,

    public final int xSize;
    public final int ySize;
    public final int zSize;

    public LocalMap(int xSize, int ySize, int zSize) {
        blockType = new BlockTypeMap(xSize, ySize, zSize);
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        light = new LightMap(this);
        bounds = new Int3dBounds(0, 0, 0, xSize - 1, ySize - 1, zSize - 1);
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

    public boolean isFlyPassable(int x, int y, int z) {
        //TODO
        return inMap(x, y, z) && blockType.getEnumValue(x, y, z).PASSING != IMPASSABLE;
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        init();
    }

    public void updateTile(Position position) {
        updatePassage(position);
        if (localTileMapUpdater != null) localTileMapUpdater.updateTile(position);
    }

    public void updatePassage(Position position) {
        if (passageMap != null) passageMap.updater.update(position.x, position.y, position.z);
    }

    public Int3dBounds getBounds() {
        return bounds;
    }
}