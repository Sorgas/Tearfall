package stonering.game.model.system.building;

import static stonering.enums.blocks.PassageEnum.PASSABLE;

import stonering.entity.building.BuildingBlock;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.util.geometry.PositionUtil;
import stonering.util.lang.Updatable;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.EntityContainer;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.ModelComponent;
import stonering.generators.buildings.BuildingGenerator;
import stonering.entity.building.Building;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

import java.util.*;

/**
 * Contains all Buildings on localMap.
 * Buildings are stored in the list as a whole entities. Building blocks stored in a map for rendering.
 *
 * @author Alexander Kuzyakov on 09.12.2017.
 */
public class BuildingContainer extends EntityContainer<Building> implements ModelComponent, Updatable {
    public final BuildingGenerator buildingGenerator;
    public final HashMap<Position, BuildingBlock> buildingBlocks;
    private List<Building> removedBuildings;
    public final WorkbenchSystem workbenchSystem;
    private final Position cachePosition;
    private LocalMap map;

    public BuildingContainer() {
        buildingBlocks = new HashMap<>();
        buildingGenerator = new BuildingGenerator();
        removedBuildings = new ArrayList<>();
        addSystem(workbenchSystem = new WorkbenchSystem());
        cachePosition = new Position();
    }

    @Override
    public void update(TimeUnitEnum unit) {
        removeMarkedForDelete();
        super.update(unit);
    }

    private void removeMarkedForDelete() {
        objects.removeAll(removedBuildings);
        removedBuildings.forEach(building -> building.iterateBlocks(block -> buildingBlocks.remove(block.position)));
    }

    /**
     * Adds building and all its blocks to container. Checks only intersections with existing buildings.
     */
    public void addBuilding(Building building) {
        if(building == null) {
            Logger.BUILDING.logWarn("Attempt to add null building to container.");
            return;
        }
        for (BuildingBlock[] blocks : building.blocks) {
            for (BuildingBlock block : blocks) {
                if (!buildingBlocks.containsKey(block.position)) continue;
                Logger.BUILDING.logError("Building is placed on another building in " + block.position);
                return; // adding failed
            }
        }
        // put blocks into container
        building.iterateBlocks(block -> buildingBlocks.put(block.position, block));
        building.iterateBlocks(block -> GameMvc.model().get(LocalMap.class).updatePassage(block.position));
        objects.add(building);
        tryMoveItems(building);
    }

    /**
     * Moves item from the target tile to neighbour one.
     */
    private void tryMoveItems(Building building) {
        ItemContainer container = GameMvc.model().get(ItemContainer.class);
        building.iterateBlocks(block -> {
            if (container.getItemsInPosition(block.position).isEmpty()) return; // no items in target position
            Position newPosition = PositionUtil.allNeighbour.stream()
                    .map(pos -> Position.add(block.position, pos))
                    .filter(position -> map().passageMap.passage.get(position) == PASSABLE.VALUE)
                    .findAny().orElse(null);
            container.getItemsInPosition(block.position).forEach(item -> container.onMapItemsSystem.changeItemPosition(item, newPosition));
        });
    }

    public Building getBuilding(Position position) {
        return Optional.ofNullable(buildingBlocks.get(position))
                .map(block -> block.building)
                .orElse(null);
    }

    public Building getBuilding(int x, int y, int z) {
        return getBuilding(cachePosition.set(x, y, z));
    }

    private LocalMap map() {
        return map == null ? map = GameMvc.model().get(LocalMap.class) : map;
    }
}
