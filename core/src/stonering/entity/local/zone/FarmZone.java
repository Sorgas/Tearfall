package stonering.entity.local.zone;

import stonering.entity.jobs.Task;
import stonering.entity.jobs.actions.PlantingAction;
import stonering.entity.jobs.actions.TaskTypesEnum;
import stonering.entity.jobs.actions.target.PositionActionTarget;
import stonering.entity.local.environment.GameCalendar;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.items.selectors.SeedItemSelector;
import stonering.entity.local.plants.PlantBlock;
import stonering.enums.ZoneTypesEnum;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.enums.plants.PlantType;
import stonering.game.GameMvc;
import stonering.game.model.lists.TaskContainer;
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

    public FarmZone(String name, Set<Position> tiles) {
        super(name, tiles);
        initZone();
    }

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
        for (Position tile : tiles) {
            byte tileType = localMap.getBlockType(position);
            if (tileType != BlockTypesEnum.FARM.CODE && tileType != BlockTypesEnum.FLOOR.CODE) { // non-floor tiles are ignored and removed from zone.
                removeTileFromZone(tile);
                continue;
            }
            // tile is already designated for something. building or digging in zones is allowed, non-floor tiles will be removed on next iteration.
            if (taskContainer.getActiveTask(tile) != null) continue;
            PlantBlock plantBlock = localMap.getPlantBlock(tile);
            if (plantBlock != null && !plants.contains(plantBlock.getPlant().getType().getTitle())) { // unwanted plant is present
                taskContainer.submitOrderDesignation(tile, DesignationTypeEnum.CUT, 1);
                continue;
            }
            if (tileType == BlockTypesEnum.FLOOR.CODE) {
                taskContainer.submitOrderDesignation(position, DesignationTypeEnum.FARM, 1);
                continue;
            }
            if (plantBlock == null) {
                createTaskForPlanting(tile, type);
            }
        }
    }

    /**
     * Creates planting task and adds it to TaskContainer.
     */
    private void createTaskForPlanting(Position position, PlantType type) {
        SeedItemSelector selector = new SeedItemSelector(type.getName());
        PlantingAction action = new PlantingAction(new PositionActionTarget(position, true, true), selector);
        Task task = new Task("plant " + type.getName(), TaskTypesEnum.DESIGNATION, action, 1);
        GameMvc.instance().getModel().get(TaskContainer.class).getTasks().add(task);
    }

    /**
     * Selects plant from enabled list for planting in current month.
     */
    private PlantType getPlantForPlanting() {
        int currentMonth = GameMvc.instance().getModel().get(GameCalendar.class).getMonth();
        for (PlantType plantType : plants) {
            if (plantType.getPlantingStart().contains(currentMonth)) return plantType;
        }
        return null;
    }

    /**
     * Updates set of months, to have only months, where plants should be planted.
     */
    public void updateMonths() {
        months.clear();
        for (PlantType plantType : plants) {
            if (plantType.getPlantingStart() != null) {
                months.addAll(plantType.getPlantingStart());
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
