package stonering.game.model.local_map.passage;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.local_map.ByteArrayWithCounter;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.PlantContainer;
import stonering.game.model.util.UtilByteArray;
import stonering.util.geometry.Position;

import static stonering.enums.blocks.BlockTypesEnum.*;
import static stonering.enums.blocks.BlockTypesEnum.PassageEnum.PASSABLE;

/**
 * Sub-component of {@link LocalMap}, is created on local map init.
 * Manages isolated areas on localMap to prevent pathfinding between them.
 * Updates areas on local map change.
 * When tile becomes passable, some areas may merge.
 * When tile becomes impassable, some areas may split.
 * Building walls performed in two steps, building floor above first, then building wall itself.
 * (for proper area update)
 * Destroying walls performed in opposite direction.
 *
 * @author Alexander on 05.11.2018.
 */
public class PassageMap {
    private LocalMap localMap;
    public PassageUpdater updater;
    public PassageUtil util;

    public ByteArrayWithCounter area; // number of area
    public UtilByteArray passage; // see {@link BlockTypesEnum} for passage values.

    private Position cachePosition;

    public PassageMap(LocalMap localMap) {
        this.localMap = localMap;
        cachePosition = new Position();
        area = new ByteArrayWithCounter(localMap.xSize, localMap.ySize, localMap.zSize);
        passage = new UtilByteArray(localMap.xSize, localMap.ySize, localMap.zSize);
        updater = new PassageUpdater(localMap, this);
        util = new PassageUtil(localMap, this);
    }

    /**
     * Resets values to the whole map.
     */
    public void init() {
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < localMap.zSize; z++) {
                    passage.set(x, y, z, isTilePassable(cachePosition.set(x, y, z)));
                }
            }
        }
        new AreaInitializer(localMap).formPassageMap(this);
    }

    /**
     * Checks that walking creature can move from one tile to another.
     * Tiles should be adjacent.
     */
    public boolean hasPathBetweenNeighbours(int x1, int y1, int z1, int x2, int y2, int z2) {
        if (!localMap.inMap(x1, y1, z1) || !localMap.inMap(x2, y2, z2)) return false; // out of map
        if (passage.get(x1, y1, z1) != PASSABLE.VALUE) return false; // cell not passable
        if (passage.get(x2, y2, z2) != PASSABLE.VALUE) return false; // cell not passable
        if (z1 == z2) return true; // passable tiles on same level
        BlockTypesEnum lower = BlockTypesEnum.getType(z1 < z2 ? localMap.getBlockType(x1, y1, z1) : localMap.getBlockType(x2, y2, z2));
        if (x1 != x2 || y1 != y2) { // check ramps
            return lower == RAMP && (x1 == x2 || y1 == y2); // lower tile is ramp, and transition is not diagonal
        } else { // check stairs
            BlockTypesEnum upper = BlockTypesEnum.getType(z1 > z2 ? localMap.getBlockType(x1, y1, z1) : localMap.getBlockType(x2, y2, z2));
            return (upper == STAIRS || upper == DOWNSTAIRS) && lower == STAIRS;
        }
    }

    public boolean hasPathBetweenNeighbours(Position from, Position to) {
        return hasPathBetweenNeighbours(from.x, from.y, from.z, to.x, to.y, to.z);
    }

    public boolean hasPathBetweenNeighbours(Position from, int x2, int y2, int z2) {
        return hasPathBetweenNeighbours(from.x, from.y, from.z, x2, y2, z2);
    }

    /**
     * Tile is passable, if its block type allows walking(like floor, ramp, etc.), plant is passable(not tree trunk), building is passable.
     * TODO add water depth checking, etc.
     */
    public int isTilePassable(Position position) {
        GameModel model = GameMvc.instance().getModel();
        PassageEnum tilePassage = getType(localMap.getBlockType(position)).PASSING;
        if(tilePassage == PassageEnum.IMPASSABLE) return PassageEnum.IMPASSABLE.VALUE;
        PlantContainer plantContainer = model.get(PlantContainer.class);
        BuildingContainer buildingContainer = model.get(BuildingContainer.class);
        if (plantContainer != null && !plantContainer.isPlantBlockPassable(position)) return PassageEnum.IMPASSABLE.VALUE;
        if (buildingContainer != null && buildingContainer.getBuildingBlocks().containsKey(position)
                && !buildingContainer.getBuildingBlocks().get(position).isPassable()) return PassageEnum.IMPASSABLE.VALUE;
        return tilePassage.VALUE;
    }

    public byte getPassage(int x, int y, int z) {
        return passage.get(x, y, z);
    }

    public UtilByteArray getArea() {
        return area;
    }
}
