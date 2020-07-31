package stonering.game.model.system.zone;

import java.util.stream.Collectors;

import stonering.entity.plant.AbstractPlant;
import stonering.entity.plant.Plant;
import stonering.entity.zone.Zone;
import stonering.entity.zone.aspect.FarmAspect;
import stonering.enums.ZoneTypeEnum;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.EntitySystem;
import stonering.game.model.system.ZoneContainer;
import stonering.game.model.system.plant.PlantContainer;
import stonering.game.model.system.task.TaskContainer;
import stonering.util.geometry.Position;
import stonering.util.validation.zone.farm.FarmPositionValidator;

/**
 * System for updating farms.
 * Removes tiles that become invalid from the zone.
 * Creates designation for cutting plants that are not specified for the zone.
 * Creates designation for preparing soil for planting(hoeing).
 * Creates designation for planting plants.
 *
 * @author Alexander on 13.07.2020.
 */
public class FarmZoneSystem extends EntitySystem<Zone> {
    private final ZoneContainer container;
    private FarmPositionValidator validator = new FarmPositionValidator();
    private PlantContainer plantContainer;
    private TaskContainer taskContainer;

    private LocalMap map;

    public FarmZoneSystem(ZoneContainer container) {
        targetAspects.add(FarmAspect.class);
        this.container = container;
    }

    @Override
    public void update(Zone zone) {
        FarmAspect aspect = zone.get(FarmAspect.class);
        if (aspect.plantType == null) return; // no plants set for farm
        // remove invalid tiles
        zone.tiles.stream()
                .filter(tile -> !ZoneTypeEnum.FARM.VALIDATOR.apply(tile))
                .collect(Collectors.toList())
                .forEach(tile -> container.setTileToZone(null, tile));
        // try create designations for tiles
        zone.tiles.stream()
                .filter(tile -> !taskContainer().designations.containsKey(tile))
                .forEach(tile -> {
                    if (tryCreateTaskForCutting(tile, aspect)) return;
                    if (tryCreateTaskForHoeing(tile)) return;
                    tryCreateTaskForPlanting(tile, aspect);
                });
    }

    //TODO use harvest designation for plant with products
    private boolean tryCreateTaskForCutting(Position tile, FarmAspect aspect) {
        AbstractPlant plant = plantContainer().getPlantInPosition(tile);
        if (plant == null || aspect.plantType.equals(plant.type.name)) return false;
        taskContainer().designationSystem.submitDesignation(tile, DesignationTypeEnum.D_CUT_FARM);
        return true;
    }

    private boolean tryCreateTaskForHoeing(Position tile) {
        if (!validator.apply(tile)) return false;
        if (map().blockType.get(tile) != BlockTypeEnum.FLOOR.CODE) return false;
        taskContainer().designationSystem.submitDesignation(tile, DesignationTypeEnum.D_HOE);
        return true;
    }

    private boolean tryCreateTaskForPlanting(Position tile, FarmAspect aspect) {
        if (map().blockType.get(tile) != BlockTypeEnum.FARM.CODE) return false;
        AbstractPlant plant = plantContainer().getPlantInPosition(tile);
        if(plant != null && plant.type.name.equals(aspect.plantType)) return false; // plant already planted
        taskContainer().designationSystem.submitPlantingDesignation(tile, aspect.plantType);
        return true;
    }

    private PlantContainer plantContainer() {
        return plantContainer == null ? plantContainer = GameMvc.model().get(PlantContainer.class) : plantContainer;
    }

    private TaskContainer taskContainer() {
        return taskContainer == null ? taskContainer = GameMvc.model().get(TaskContainer.class) : taskContainer;
    }

    private LocalMap map() {
        return map == null ? map = GameMvc.model().get(LocalMap.class) : map;
    }
}
