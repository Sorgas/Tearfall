package stonering.game.model.system.building;

import stonering.entity.Aspect;
import stonering.entity.building.BuildingBlock;
import stonering.enums.blocks.PassageEnum;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.Updatable;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.EntityContainer;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.ModelComponent;
import stonering.generators.buildings.BuildingGenerator;
import stonering.entity.building.Building;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    public BuildingContainer() {
        buildingBlocks = new HashMap<>();
        buildingGenerator = new BuildingGenerator();
        removedBuildings = new ArrayList<>();
        putSystem(workbenchSystem = new WorkbenchSystem());
    }

    @Override
    public void update(TimeUnitEnum unit) {
        removeMarkedForDelete();
        super.update(unit);
    }

    private void removeMarkedForDelete() {
        entities.removeAll(removedBuildings);
        for (Building building : removedBuildings) {
            for (BuildingBlock[] blocks : building.blocks) {
                for (BuildingBlock block : blocks) {
                    buildingBlocks.remove(block.position);
                }
            }
        }
    }

    /**
     * Adds building and all its blocks to container. Checks only intersections with existing buildings.
     */
    public void addBuilding(Building building) {
        for (BuildingBlock[] blocks : building.blocks) {
            for (BuildingBlock block : blocks) {
                if (!buildingBlocks.containsKey(block.position)) continue;
                Logger.BUILDING.logError("Building is placed on another building in " + block.position);
                return; // adding failed
            }
        }
        for (BuildingBlock[] blocks : building.blocks) {
            for (BuildingBlock block : blocks) {
                buildingBlocks.put(block.position, block); // add block
            }
        }
        for (BuildingBlock[] blocks : building.blocks) {
            for (BuildingBlock block : blocks) {
                GameMvc.model().get(LocalMap.class).updateTile(building.position);
            }
        }
        entities.add(building);
        tryMoveItems(building);
    }

    /**
     * Moves item from the target tile to neighbour one.
     */
    private void tryMoveItems(Building building) {
        ItemContainer container = GameMvc.model().get(ItemContainer.class);
        for (BuildingBlock[] blocks : building.blocks) {
            for (BuildingBlock block : blocks) {
                Position target = block.position;
                if (container.getItemsInPosition(target).isEmpty()) return; // no items in target position
                Position newPosition = GameMvc.model().get(LocalMap.class).getAnyNeighbourPosition(target, PassageEnum.PASSABLE);
                if (newPosition.equals(target)) return; // no moving, if no valid position found
                container.getItemsInPosition(target).forEach(item -> container.onMapItemsSystem.changeItemPosition(item, newPosition));
            }
        }
    }

    public boolean hasBuilding(Position position) {
        return buildingBlocks.containsKey(position);
    }

    public Building getBuiding(Position position) {
        if (!hasBuilding(position)) return null;
        return buildingBlocks.get(position).building;
    }

    public List<Building> getBuildingsWithAspect(Class<? extends Aspect> T) {
        return entities.stream().filter(building -> building.hasAspect(T)).collect(Collectors.toList());
    }
}
