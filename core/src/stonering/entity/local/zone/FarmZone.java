package stonering.entity.local.zone;


import stonering.designations.Designation;
import stonering.entity.jobs.Task;
import stonering.entity.local.building.validators.PositionValidator;
import stonering.entity.local.environment.GameCalendar;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.plants.PlantBlock;
import stonering.enums.ZoneTypesEnum;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.PlantType;
import stonering.game.GameMvc;
import stonering.game.model.lists.TaskContainer;
import stonering.game.model.lists.ZonesContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

import java.util.*;
import java.util.function.Consumer;

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
    private Set<String> plants; // plants selected for growing.
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
        tryCreatePlantingTasks();
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
        String plant = getPlantForPlanting();
        if (!monthForHoeing && plant == null) return;
        LocalMap localMap = GameMvc.getInstance().getModel().get(LocalMap.class);
        TaskContainer taskContainer = GameMvc.getInstance().getModel().get(TaskContainer.class);
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
            if(plantBlock == null) {
                createTaskForPlanting(tile, plant);
            }
        }
    }

    /**
     * Observes all tiles and creates tasks for planting enabled plants, or cutting other plants.
     */
    private void tryCreatePlantingTasks() {
        String plant = getPlantForPlanting();
        if (plant == null) return;
        LocalMap localMap = GameMvc.getInstance().getModel().get(LocalMap.class);
        TaskContainer taskContainer = GameMvc.getInstance().getModel().get(TaskContainer.class);
        for (Position tile : tiles) {
            taskContainer.getDesignations().get(tile) != null)continue; // tile is prepared or already designated
            taskContainer.submitOrderDesignation(tile, DesignationTypeEnum.FARM, 1);
        }
    }

    private void createTaskForPlanting(Position position, String plantName) {

    }

    /**
     * Selects plant from enabled list for planting in current month.
     */
    private String getPlantForPlanting() {
        int currentMonth = GameMvc.getInstance().getModel().get(GameCalendar.class).getMonth();
        for (String plant : plants) {
            PlantType plantType = PlantMap.getInstance().getPlantType(plant);
            if (plantType.getPlantingStart().contains(currentMonth)) return plant;
        }
        return null;
    }

    /**
     * Updates set of months, to have only months, where plants should be planted.
     */
    public void updateMonths() {
        months.clear();
        PlantMap plantMap = PlantMap.getInstance();
        for (String plant : plants) {
            PlantType type = plantMap.getPlantType(plant);
            if (type.getPlantingStart() != null) {
                months.addAll(type.getPlantingStart());
            }
        }
    }

    private boolean monthForPreparingSoil() {
        int currentMonth = GameMvc.getInstance().getModel().get(GameCalendar.class).getMonth();
        return months.contains(getNextMonth(currentMonth)) || months.contains(currentMonth);
    }

    public void enablePlant(String plantName) {
        plants.add(plantName);
    }

    public void disablePlant(String plantName) {
        plants.remove(plantName);
        cancelGrowingTasks(plantName);
    }

    private void cancelGrowingTasks(String plantName) {
        //TODO
    }

    private int getNextMonth(int current) {
        return current >= 11 ? 0 : current + 1;
    }

    public Set<String> getPlants() {
        return plants;
    }

    /**
     * Removes single tile from this zone.
     * Zone modification is fully handled through container.
     */
    private void removeTileFromZone(Position position) {
        GameMvc.getInstance().getModel().get(ZonesContainer.class).updateZones(position, position, null);
    }
}
