package stonering.game.model.lists;

import stonering.entity.local.building.BuildingBlock;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.ModelComponent;
import stonering.game.model.Turnable;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.buildings.BuildingGenerator;
import stonering.entity.local.building.Building;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Contains all Buildings on localMap
 *
 * @author Alexander Kuzyakov on 09.12.2017.
 */
public class BuildingContainer extends Turnable implements ModelComponent, Initable {
    private List<Building> buildings;
    private BuildingGenerator buildingGenerator;
    private HashMap<Position, BuildingBlock> buildingBlocks;

    public BuildingContainer() {
        this(new ArrayList<>());
    }

    public BuildingContainer(List<Building> buildings) {
        buildingBlocks = new HashMap<>();
        buildingGenerator = new BuildingGenerator();
        this.buildings = buildings;
    }

    public void init() {
        buildings.forEach(this::placeBuilding);
    }

    /**
     * Places building block on local map.
     */
    private void placeBuilding(Building building) {
        building.init();
        buildingBlocks.put(building.getBlock().getPosition(), building.getBlock());
        tryMoveItems(building.getBlock().getPosition());
    }

    /**
     * Moves item from the target tile to neighbour one.
     */
    private void tryMoveItems(Position target) {
        ItemContainer container = GameMvc.instance().getModel().get(ItemContainer.class);
        if (container.getItemsInPosition(target).isEmpty()) return; // no items in target position
        Position newPosition = GameMvc.instance().getModel().get(LocalMap.class).getAnyNeighbourPosition(target, BlockTypesEnum.PASSABLE);
        if (newPosition.equals(target)) return; // no moving, if no valid position found
        container.getItemsInPosition(target).forEach(item -> container.moveItem(item, newPosition));
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void turn() {
        for (Building building : buildings) {
            building.turn();
        }
    }

    /**
     * Adds building to container and places it on map.
     */
    public void addBuilding(Building building) {
        buildings.add(building);
        placeBuilding(building);
    }

    public BuildingGenerator getBuildingGenerator() {
        return buildingGenerator;
    }

    public HashMap<Position, BuildingBlock> getBuildingBlocks() {
        return buildingBlocks;
    }

    public boolean isBlockPassable(Position position) {
        //TODO add passage enum
        return !buildingBlocks.containsKey(position) || buildingBlocks.get(position).getPassage().equals("floor");
    }
}