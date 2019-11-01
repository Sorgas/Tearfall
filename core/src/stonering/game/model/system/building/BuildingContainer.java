package stonering.game.model.system.building;

import stonering.entity.Aspect;
import stonering.entity.building.BuildingBlock;
import stonering.game.GameMvc;
import stonering.game.model.Turnable;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.ModelComponent;
import stonering.generators.buildings.BuildingGenerator;
import stonering.entity.building.Building;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static stonering.enums.blocks.BlockTypesEnum.PassageEnum.PASSABLE;

/**
 * Contains all Buildings on localMap.
 * Buildings are stored in the list as a whole entities. Building blocks stored in a map for rendering.
 *
 * @author Alexander Kuzyakov on 09.12.2017.
 */
public class BuildingContainer implements ModelComponent, Turnable {
    private List<Building> buildings;
    public final BuildingGenerator buildingGenerator;
    private HashMap<Position, BuildingBlock> buildingBlocks;
    private List<Building> removedBuildings;
    public final WorkbenchSystem workbenchSystem;

    public BuildingContainer() {
        buildingBlocks = new HashMap<>();
        buildingGenerator = new BuildingGenerator();
        this.buildings = new ArrayList<>();
        removedBuildings = new ArrayList<>();
        workbenchSystem = new WorkbenchSystem();
    }

    /**
     * Every tick.
     */
    public void turn() {
        removeMarkedForDelete();
        for (Building building : buildings) {
            building.turn();
            workbenchSystem.updateWorkbenchState(building);
        }
    }

    private void removeMarkedForDelete() {
        buildings.removeAll(removedBuildings);
        for (Building removedBuilding : removedBuildings) {
            buildingBlocks.remove(removedBuilding.getBlock().getPosition());
        }
    }

    /**
     * Adds building to container and places it on map.
     */
    public void addBuilding(Building building) {
        building.init();
        buildings.add(building);
        buildingBlocks.put(building.getBlock().getPosition(), building.getBlock());
        tryMoveItems(building.getBlock().getPosition());
    }

    /**
     * Removes building from container and from map.
     */
    public void removeBuilding(Building building) {
        buildings.remove(building);
        buildingBlocks.remove(building.getBlock().getPosition());
    }

    /**
     * Moves item from the target tile to neighbour one.
     */
    private void tryMoveItems(Position target) {
        ItemContainer container = GameMvc.instance().getModel().get(ItemContainer.class);
        if (container.getItemsInPosition(target).isEmpty()) return; // no items in target position
        Position newPosition = GameMvc.instance().getModel().get(LocalMap.class).getAnyNeighbourPosition(target, PASSABLE);
        if (newPosition.equals(target)) return; // no moving, if no valid position found
        container.getItemsInPosition(target).forEach(item -> container.moveItem(item, newPosition));
    }

    public boolean hasBuilding(Position position) {
        return buildingBlocks.containsKey(position);
    }

    public Building getBuiding(Position position) {
        if (!hasBuilding(position)) return null;
        return buildingBlocks.get(position).getBuilding();
    }

    public HashMap<Position, BuildingBlock> getBuildingBlocks() {
        return buildingBlocks;
    }

    public List<Building> getBuildingsWithAspect(Class<? extends Aspect> T) {
        return buildings.stream().filter(building -> building.hasAspect(T)).collect(Collectors.toList());
    }
}
