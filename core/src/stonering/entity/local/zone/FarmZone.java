package stonering.entity.local.zone;

import stonering.entity.job.Task;
import stonering.entity.job.action.PlantingAction;
import stonering.entity.job.action.TaskTypesEnum;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.local.plants.AbstractPlant;
import stonering.game.model.lists.PlantContainer;
import stonering.util.validation.PositionValidator;
import stonering.entity.local.environment.GameCalendar;
import stonering.entity.local.item.selectors.ItemSelector;
import stonering.entity.local.item.selectors.SeedItemSelector;
import stonering.enums.ZoneTypesEnum;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.enums.plants.PlantType;
import stonering.game.GameMvc;
import stonering.game.model.lists.tasks.TaskContainer;
import stonering.game.model.lists.ZonesContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Farm keeps track on plants condition in its zone, and create tasks respectively.
 * Farm have list of enabled plants and list of months;
 * when current game month in one from the list, farm will generate tasks for planting seeds.
 * <p>
 * Farm generates tasks for each tile of farm separately. For performance, it can commit only one task per turn.
 *
 * @author Alexander on 04.03.2019.
 */
public class FarmZone extends Zone {
    private Set<PlantType> plants; // plants selected for growing.
    private Set<Integer> months; // planting tasks are created only in these months
    private ItemSelector seedSelector; // planting tasks share this selector. it is updated as months change.
    private Map<Position, Task> hoeingTasksMap;

    public FarmZone(String name) {
        super(name);
        initZone();
    }

    private void initZone() {
        plants = new HashSet<>();
        months = new HashSet<>();
        type = ZoneTypesEnum.FARM;
        hoeingTasksMap = new HashMap<>();
    }

    @Override
    public void turn() {
        checkTiles();
    }

    /**
     * Observes all tiles and creates tasks.
     * 1. Designates unprepared soil floor tiles for hoeing.
     * 2. Designates existent plants not from enabled list for cutting.
     * 3. Designates prepared tiles(1) for planting enabled plants.
     * Soil preparation begins one month before first plant can be planted, disregarding seed availability.
     */
    private void checkTiles() {
        boolean monthForHoeing = monthForPreparingSoil();
        PlantType type = getPlantForPlanting();
        if (!monthForHoeing && type == null) return;
        LocalMap localMap = GameMvc.instance().getModel().get(LocalMap.class);
        TaskContainer taskContainer = GameMvc.instance().getModel().get(TaskContainer.class);
        PositionValidator validator = ZoneTypesEnum.FARM.getValidator();
        PlantContainer plantContainer = GameMvc.instance().getModel().get(PlantContainer.class);
        for (Position tile : tiles) {
            byte tileType = localMap.getBlockType(tile);
            // non-floor tiles are ignored and removed from zone. This can occur when tile is dug out, built on or under colla
            if (tileType != BlockTypesEnum.FARM.CODE && tileType != BlockTypesEnum.FLOOR.CODE) {
                removeTileFromZone(tile);
                continue;
            }
            // tile is already designated for something. building or digging in zones is allowed, non-floor tiles will be removed on next iteration.
            if (taskContainer.getActiveTask(tile) != null) continue;
            AbstractPlant plant = plantContainer.getPlantInPosition(tile);
            if (plant != null && !plants.contains(plant.getType().title)) { // unwanted plant is present, cut
                taskContainer.submitOrderDesignation(tile, DesignationTypeEnum.CUT, 1);
                continue;
            }
            if (validator.validate(localMap, tile)) { // prepare soil
                taskContainer.submitOrderDesignation(tile, DesignationTypeEnum.HOE, 1);
                continue;
            }
            if (plant == null) { // plant new plant
                createTaskForPlanting(tile, type);
            }
        }
    }

    /**
     * Creates planting task and adds it to TaskContainer.
     */
    private void createTaskForPlanting(Position position, PlantType type) {
        SeedItemSelector selector = new SeedItemSelector(type.name);
        PlantingAction action = new PlantingAction(new PositionActionTarget(position, true, true), selector);
        Task task = new Task("plant " + type.name, TaskTypesEnum.DESIGNATION, action, 1);
        GameMvc.instance().getModel().get(TaskContainer.class).getTasks().add(task);
    }

    /**
     * Selects plant from enabled list for planting in current month.
     */
    private PlantType getPlantForPlanting() {
        int currentMonth = GameMvc.instance().getModel().get(GameCalendar.class).getMonth();
        for (PlantType plantType : plants) {
            if (plantType.plantingStart.contains(currentMonth)) return plantType;
        }
        return null;
    }

    /**
     * Updates set of months, to have only months, where plants should be planted.
     */
    public void updateMonths() {
        months.clear();
        for (PlantType plantType : plants) {
            if (plantType.plantingStart != null) {
                months.addAll(plantType.plantingStart);
            }
        }
    }

    private boolean monthForPreparingSoil() {
        int currentMonth = GameMvc.instance().getModel().get(GameCalendar.class).getMonth();
        return months.contains(getNextMonth(currentMonth)) || months.contains(currentMonth);
    }

    public void setPlant(PlantType plantType, boolean status) {
        if(status) {
            plants.add(plantType);
        } else {
            plants.remove(plantType);
            cancelGrowingTasks(plantType);
        }
        updateMonths();
    }

    private void cancelGrowingTasks(PlantType plantType) {
        //TODO
    }

    private int getNextMonth(int current) {
        return current >= 11 ? 0 : current + 1;
    }

    public Set<PlantType> getPlants() {
        return new HashSet<>(plants);
    }

    /**
     * Removes single tile from this zone.
     * Zone modification is fully handled through container.
     */
    private void removeTileFromZone(Position position) {
        GameMvc.instance().getModel().get(ZonesContainer.class).updateZones(position, position, null);
    }
}
