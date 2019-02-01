package stonering.game.core.model.util;

import stonering.entity.local.building.BuildingBlock;
import stonering.entity.local.plants.PlantBlock;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.model.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.global.Pair;
import stonering.util.global.TagLoggersEnum;

import java.util.*;

/**
 * Manages isolated areas on localMap to prevent pathfinding between them.
 * Updates areas on local map change.
 *
 * @author Alexander on 05.11.2018.
 */
public class PassageMap {
    private LocalMap localMap;
    private byte[][][] area; // number of area
    private byte[][][] passage; // stores

    public PassageMap(LocalMap localMap) {
        this.localMap = localMap;
        area = new byte[localMap.getxSize()][localMap.getySize()][localMap.getzSize()];
        passage = new byte[localMap.getxSize()][localMap.getySize()][localMap.getzSize()];
    }

    /**
     * Called when local map passage is updated. If cell becomes non-passable, it may split area into two.
     */
    public void updateCell(int x, int y, int z) {
        passage[x][y][z] = (byte) (isWalkPassable(x, y, z) ? 1 : 0);

        if (passage[x][y][z] == 0) { // cell becomes non-passable

        } else {

        }
    }

    private Set<Byte> observeAreasAround(int cx, int cy, int cz) {
        Set<Byte> neighbours = new HashSet<>();
        for (int x = cx - 1; x < cx + 2; x++) {
            for (int y = cy - 1; y < cy + 2; y++) {
                for (int z = cz - 1; z < cz + 2; z++) {

                    byte currentArea = area[x][y][z];
                    if (currentArea != 0) neighbours.add(currentArea);
                }
            }
        }
        return neighbours;
    }

    public void initPassage() {
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getySize(); y++) {
                for (int z = 0; z < localMap.getzSize(); z++) {
                    passage[x][y][z] = (byte) (isWalkPassable(x, y, z) ? 1 : 0);
                }
            }
        }
    }

    //sells should be adjacent
    private boolean hasPathBetween(int x, int y, int z, int x2, int y2, int z2) {
        if (isWalkPassable(x, y, z) && isWalkPassable(x2, y2, z2)) {
            if (z == z2) {
                return true;
            } else if (x == x2 && y == y2) {
                return localMap.isWorkingStair(x, y, Math.min(z, z2));
            } else {
                if (z < z2) {
                    return localMap.isWorkingRamp(x, y, z);
                } else {
                    return localMap.isWorkingRamp(x2, y2, z2);
                }
            }
        }
        return false;
    }

    public boolean isWalkPassable(Position pos) {
        return isWalkPassable(pos.getX(), pos.getY(), pos.getZ());
    }

    public boolean isWalkPassable(int x, int y, int z) {
        return BlockTypesEnum.getType(localMap.getBlockType(x, y, z)).getPassing() == 2;
    }

    public boolean isFlyPassable(Position pos) {
        return isFlyPassable(pos.getX(), pos.getY(), pos.getZ());
    }

    public boolean isFlyPassable(int x, int y, int z) {
        return BlockTypesEnum.getType(localMap.getBlockType(x, y, z)).getPassing() != 0; // 1 || 2
    }

    public byte getArea(Position pos) {
        return area[pos.getX()][pos.getY()][pos.getZ()];
    }

    public byte getArea(int x, int y, int z) {
        return area[x][y][z];
    }

    public void setArea(int x, int y, int z, byte value) {
        area[x][y][z] = value;
    }

    public void update(int x, int y, int z, byte type) {

    }

    public void update(int x, int y, int z, BuildingBlock block) {
    }

    public void update(int x, int y, int z, PlantBlock plantBlock) {
    }

}
