package stonering.entity.zone;

import stonering.entity.job.Task;
import stonering.entity.job.action.PlantingAction;
import stonering.entity.job.action.TaskTypesEnum;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.plants.AbstractPlant;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.model.lists.PlantContainer;
import stonering.util.validation.PositionValidator;
import stonering.entity.environment.GameCalendar;
import stonering.entity.item.selectors.SeedItemSelector;
import stonering.enums.ZoneTypesEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.enums.plants.PlantType;
import stonering.game.GameMvc;
import stonering.game.model.lists.tasks.TaskContainer;
import stonering.game.model.lists.ZonesContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * Farm keeps track on plantType condition in its zone, and create tasks respectively.
 * Farm have list of enabled plantType and list of months; months in the list depend on the plantType selected.
 * when current game month in one from the list, farm will generate tasks for planting seeds.
 * <p>
 * Farm generates tasks for each tile of farm separately. For performance, it can commit only one task per turn.
 * // TODO farms check temperature conditions for planting, create tasks for irrigation, and caring after plants. plants can get deseases.
 *
 * @author Alexander on 04.03.2019.
 */
public class FarmZone extends Zone {
    private PlantType plantType; // plantType selected for growing.
    private SeedItemSelector seedSelector; // planting tasks share this selector. it is updated as months change.
    private Map<Position, Task> taskMap; // farm stores its tasks in this map to avoid committing duplicating tasks to container. tasks are removed when finished.

    public FarmZone(String name) {
        super(name);
        initZone();
    }

    private void initZone() {
        type = ZoneTypesEnum.FARM;
        taskMap = new HashMap<>();
    }

    @Override
    public void turn() {
        checkTiles();
    }

    /**
     * Observes all tiles and creates tasks.
     * 1. Designates unprepared soil floor tiles for hoeing.
     * 2. Designates existent plantType not from enabled list for cutting.
     * 3. Designates prepared tiles(1) for planting enabled plantType.
     * Soil preparation begins one month before first plant can be planted, disregarding seed availability.
     */
    private void checkTiles() {
        if (plantType == null) return; // no plant set for farm
        int currentMonth = GameMvc.instance().getModel().get(GameCalendar.class).getMonth();
        boolean plantingEnabled = plantType.plantingStart.contains(currentMonth);
        boolean hoeingEnabled = plantingEnabled || plantType.plantingStart.contains((currentMonth + 1) % 12);
        LocalMap localMap = GameMvc.instance().getModel().get(LocalMap.class);
        TaskContainer taskContainer = GameMvc.instance().getModel().get(TaskContainer.class);
        PlantContainer plantContainer = GameMvc.instance().getModel().get(PlantContainer.class);
        PositionValidator validator = ZoneTypesEnum.FARM.getValidator();
        for (Position tile : tiles) {
            AbstractPlant plant = plantContainer.getPlantInPosition(tile);
            // can delete tile from zone
            if (!checkTile(validator, tile, localMap)) continue;
            // can delete task from zone
            if (!checkTask(tile)) continue;
            // can create task for cutting or harvesting
            if (!checkExistingPlant(plant, tile, taskContainer)) continue;
            if (localMap.getBlockType(tile) != BlockTypesEnum.FARM.CODE) {
                if (hoeingEnabled) taskContainer.submitOrderDesignation(tile, DesignationTypeEnum.HOE, 1);
            } else {
                if (plantingEnabled) createTaskForPlanting(tile, plantType);
            }
        }
    }

    /**
     * Checks that tile can hold a plant (floor or farm, soil, no buildings).
     * Building or digging in zones are allowed, non-floor tiles are removed on every iteration.
     */
    private boolean checkTile(PositionValidator validator, Position tile, LocalMap localMap) {
        if (validator.validate(localMap, tile)) return true;
        GameMvc.instance().getModel().get(ZonesContainer.class).updateZones(tile, tile, null); // remove invalid tile
        return false;
    }

    /**
     * Checks that task for this tile is created or not yet finished.
     *
     * @return true, if task can be created after this method.
     */
    private boolean checkTask(Position tile) {
        if (!taskMap.containsKey(tile)) return false; // no task
        if (!taskMap.get(tile).isFinished()) return true; // active task
        taskMap.remove(tile); // finished task, remove. tasks are removed from container on finish
        return false;
    }

    /**
     * Checks plant that currently exists on the tile. Creates task for cutting if needed.
     *
     * @return true, if task can be created after this method.
     */
    private boolean checkExistingPlant(AbstractPlant plant, Position tile, TaskContainer container) {
        if (plant == null) return true;
        Task task = null;
        if (plant.isHarvestable()) { // harvest any plants
            task = container.submitOrderDesignation(tile, DesignationTypeEnum.HARVEST, 1);
        } else if (!plantType.equals(plant.getType())) { // cut unwanted plants
            task = container.submitOrderDesignation(tile, DesignationTypeEnum.CUT, 1);
        }
        if (task != null) taskMap.put(tile, task);
        return false;
    }

    /**
     * Creates planting task and adds it to TaskContainer.
     */
    private void createTaskForPlanting(Position tile, PlantType type) {
        PlantingAction action = new PlantingAction(new PositionActionTarget(tile, true, true), seedSelector);
        Task task = new Task("plant " + type.name, TaskTypesEnum.DESIGNATION, action, 1);
        GameMvc.instance().getModel().get(TaskContainer.class).getTasks().add(task);
    }

    public void setPlant(PlantType plantType) {
        this.plantType = plantType;
        if (plantType != null) seedSelector = new SeedItemSelector(plantType.name);
    }

    public PlantType getPlantType() {
        return plantType;
    }
}
