package stonering.game.model.local_map.passage;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.blocks.PassageEnum;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.local_map.BlockTypeMap;
import stonering.game.model.local_map.ByteArrayWithCounter;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.liquid.LiquidContainer;
import stonering.game.model.system.plant.PlantContainer;
import stonering.util.UtilByteArray;
import stonering.util.geometry.Position;

import static stonering.enums.blocks.BlockTypeEnum.*;
import static stonering.enums.blocks.PassageEnum.IMPASSABLE;
import static stonering.enums.blocks.PassageEnum.PASSABLE;

import java.util.Optional;

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
    private final LocalMap localMap;
    private final BlockTypeMap blockTypeMap;
    public PassageUpdater updater;
    public PassageUtil util;

    public ByteArrayWithCounter area; // number of area
    public UtilByteArray passage; // see {@link BlockTypesEnum} for passage values.

    private Position cachePosition;

    public PassageMap(LocalMap localMap) {
        this.localMap = localMap;
        blockTypeMap = localMap.blockType;
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
        localMap.bounds.iterate((x, y, z) -> passage.set(x, y, z, calculateTilePassage(cachePosition.set(x, y, z)).VALUE));
        new AreaInitializer(localMap).formPassageMap(this);
    }

    /**
     * Checks that walking creature can move from one tile to another.
     * Tiles should be adjacent.
     */
    public boolean hasPathBetweenNeighbours(int x1, int y1, int z1, int x2, int y2, int z2) {
        if (!localMap.inMap(x1, y1, z1) || !localMap.inMap(x2, y2, z2)) return false; // out of map
        if (passage.get(x1, y1, z1) == IMPASSABLE.VALUE || passage.get(x2, y2, z2) == IMPASSABLE.VALUE) return false;
        if (z1 == z2) return true; // passable tiles on same level

        byte lower = z1 < z2 ? blockTypeMap.get(x1, y1, z1) : blockTypeMap.get(x2, y2, z2);
        if (x1 != x2 || y1 != y2) return lower == RAMP.CODE; // handle ramps
        byte upper = z1 > z2 ? blockTypeMap.get(x1, y1, z1) : blockTypeMap.get(x2, y2, z2);
        return (upper == STAIRS.CODE || upper == DOWNSTAIRS.CODE) && lower == STAIRS.CODE; // handle stairs
    }

    /**
     * Checks that unit, standing in position will have access (to dig, open a chest) to target tile.
     * Same Z-level tiles are always accessible.
     * Tiles are accessible vertically with stairs or ramps.
     */
    public boolean tileIsAccessibleFromNeighbour(int targetX, int targetY, int targetZ, int x, int y, int z, BlockTypeEnum targetType) {
        if(!localMap.inMap(targetX, targetY, targetZ) || !localMap.inMap(x, y, z) || passage.get(x, y, z) == IMPASSABLE.VALUE) return false;
        if(targetZ == z) return true;
        BlockTypeEnum fromType = BlockTypeEnum.getType(blockTypeMap.get(x, y, z));
        BlockTypeEnum lower = targetZ < z ? targetType : fromType;
        if((targetX == x) != (targetY == y)) return lower == RAMP; // ramp near and lower
        if(targetX != x) return false;
        BlockTypeEnum upper = targetZ > z ? targetType : fromType;
        return lower == STAIRS && (upper == STAIRS || upper == DOWNSTAIRS);
    }

    public boolean tileIsAccessibleFromNeighbour(Position target, Position position, BlockTypeEnum type) {
        return tileIsAccessibleFromNeighbour(target.x, target.y, target.z, position.x, position.y, position.z, type);
    }

    public boolean hasPathBetweenNeighbours(Position from, Position to) {
        return hasPathBetweenNeighbours(from.x, from.y, from.z, to.x, to.y, to.z);
    }

    /**
     * Tile is passable, if its block type allows walking(like floor, ramp, etc.), plants are passable(not tree trunks), buildings are impassable.
     * TODO add water depth checking, etc.
     */
    public PassageEnum calculateTilePassage(Position position) {
        GameModel model = GameMvc.model();
        PassageEnum tilePassage = getType(blockTypeMap.get(position)).PASSING;
        if (tilePassage == PASSABLE) { // tile still can be blocked by plants or buildings
            boolean plantPassable = model.optional(PlantContainer.class)
                    .map(plantContainer -> plantContainer.isPlantBlockPassable(position)).orElse(true);
            if(!plantPassable) return IMPASSABLE;

            boolean buildingPassable = model.optional(BuildingContainer.class)
                    .map(container -> container.buildingBlocks.get(position))
                    .map(block -> block.passage == PASSABLE).orElse(true);
            if(!buildingPassable) return IMPASSABLE;

            boolean waterPassable = model.optional(LiquidContainer.class)
                    .map(container -> container.getTile(position))
                    .map(tile -> tile.amount <= 4).orElse(true);
            if(!waterPassable) return IMPASSABLE;
        }
        return tilePassage;
    }

    public byte getPassage(int x, int y, int z) {
        return passage.get(x, y, z);
    }

    public boolean inSameArea(Position pos1, Position pos2) {
        return localMap.inMap(pos1) && localMap.inMap(pos2) && area.get(pos1) == area.get(pos2);
    }
}
