package stonering.game.model.system.zone;

import stonering.entity.plant.AbstractPlant;
import stonering.entity.zone.Zone;
import stonering.entity.zone.aspect.FarmAspect;
import stonering.enums.ZoneTypesEnum;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.EntitySystem;
import stonering.game.model.system.ZoneContainer;
import stonering.game.model.system.plant.PlantContainer;
import stonering.game.model.system.task.TaskContainer;
import stonering.util.geometry.Position;

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
    private PlantContainer plantContainer;
    private TaskContainer taskContainer;
    private LocalMap map;

    public FarmZoneSystem() {
        targetAspects.add(FarmAspect.class);
    }

    @Override
    public void update(Zone zone) {
        FarmAspect aspect = zone.get(FarmAspect.class);
        if (aspect.plantTypes.isEmpty()) return; // no plants set for farm
        for (Position tile : zone.tiles) {
            // method chain
            boolean handled = tryRemoveInvalidTile(tile)
                    || tryCutUnwantedPlant(tile, aspect)
                    || tryCreateTaskForHoeing(tile)
                    || tryCreateTaskForPlanting(tile, aspect);
        }
    }

    private boolean tryRemoveInvalidTile(Position tile) {
        if (ZoneTypesEnum.FARM.VALIDATOR.apply(tile)) return false;
        GameMvc.model().get(ZoneContainer.class).setTileToZone(null, tile); // remove invalid tile
        return true;
    }

    //TODO use harvest designation for plant with products
    private boolean tryCutUnwantedPlant(Position tile, FarmAspect aspect) {
        AbstractPlant plant = plantContainer().getPlantInPosition(tile);
        if (plant == null || aspect.plantTypes.contains(plant.type.name)) return false;
        taskContainer().designationSystem.submitDesignation(tile, DesignationTypeEnum.D_CUT);
        return true;
    }

    private boolean tryCreateTaskForHoeing(Position tile) {
        if (map().blockType.get(tile) == BlockTypeEnum.FARM.CODE) return false;
        taskContainer().designationSystem.submitDesignation(tile, DesignationTypeEnum.D_HOE);
        return true;
    }

    private boolean tryCreateTaskForPlanting(Position tile, FarmAspect aspect) {
        if (map().blockType.get(tile) == BlockTypeEnum.FARM.CODE) return false;
        taskContainer().designationSystem.submitDesignation(tile, DesignationTypeEnum.D_PLANT);
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
