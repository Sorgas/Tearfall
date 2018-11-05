package stonering.game.core.model.util;

import stonering.entity.local.building.BuildingBlock;
import stonering.entity.local.plants.PlantBlock;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.model.LocalMap;
import stonering.global.utils.Position;
import stonering.utils.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

/**
 * Manages isolated areas on localMap to prevent pathfinding between them.
 * Inits areas on load, updates on local map change.
 *
 * @author Alexander on 05.11.2018.
 */
public class PassageMap {
    private LocalMap localMap;
    private byte[][][] area;
    private byte[][][] passage;

    private ArrayList<Byte> areaNumbers;

    public PassageMap(LocalMap localMap) {
        this.localMap = localMap;
        area = new byte[localMap.getxSize()][localMap.getySize()][localMap.getzSize()];
        passage = new byte[localMap.getxSize()][localMap.getySize()][localMap.getzSize()];
        areaNumbers = new ArrayList<>();
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

    /**
     * Gives area number to all tiles on map. isolated tiles get different numbers.
     */
    public void initAreas() {
        byte areaNum = 1;
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getySize(); y++) {
                for (int z = 0; z < localMap.getzSize(); z++) {
                    if (isWalkPassable(x, y, z) && area[x][y][z] == 0) { // not wall
                        floodFill(x, y, z, areaNum);
                        areaNumbers.add(areaNum);
                        areaNum++;
                    }
                }
            }
        }
    }

    /**
     * Fills tiles, reachable from given one with given number.
     *
     * @param sx
     * @param sy
     * @param sz
     * @param number
     */
    private void floodFill(int sx, int sy, int sz, byte number) {
        TagLoggersEnum.PATH.log("filling" + " " + sx + " " + sy + " " + sz + " with " + number);
        Stack<Position> openStack = new Stack<>();
        HashSet<Position> closedSet = new HashSet<>();
        openStack.add(new Position(sx, sy, sz));

        while (!openStack.isEmpty()) {
            Position position = openStack.pop();
            closedSet.add(position);
            int cx = position.getX();
            int cy = position.getY();
            int cz = position.getZ();
            area[cx][cy][cz] = number;
            for (int x = cx - 1; x < cx + 2; x++) {
                for (int y = cy - 1; y < cy + 2; y++) {
                    for (int z = cz - 1; z < cz + 2; z++) {
                        if ((x != cx || y != cy || z != cz) &&
                                localMap.inMap(x, y, z) &&
                                hasPathBetween(cx, cy, cz, x, y, z)) {
                            Position newPos = new Position(x, y, z);
                            if (!openStack.contains(newPos) && !closedSet.contains(newPos)) {
                                openStack.add(newPos);
                            }
                        }
                    }
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


    public void update(int x, int y, int z, byte type) {

    }

    public void update(int x, int y, int z, BuildingBlock block) {
    }

    public void update(int x, int y, int z, PlantBlock plantBlock) {
    }

}
