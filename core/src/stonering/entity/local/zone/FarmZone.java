package stonering.entity.local.zone;


import stonering.designations.Designation;
import stonering.entity.jobs.Task;
import stonering.entity.local.environment.GameCalendar;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.enums.ZoneTypesEnum;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.PlantType;
import stonering.game.core.GameMvc;
import stonering.game.core.model.lists.TaskContainer;
import stonering.util.geometry.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Farm keeps track on plants condition in its zone, and create tasks respectively.
 * Farm have list of enabled plants and list of months;
 * when current game month in one from the list, farm will generate tasks for planting seeds.
 *
 * Farm generates tasks for each tile of farm separately. For performance, it can commit only one task per turn.
 *
 * @author Alexander on 04.03.2019.
 */
public class FarmZone extends Zone {
    private Set<String> plants; // plants selected for growing.
    private Set<Integer> months; // planting tasks are created only in these months
    private ItemSelector seedSelector; // planting tasks share this selector. it is updated as months change.

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
    }

    @Override
    public void turn() {

    }

    /**
     * Passes through all tiles and creates hoeing task for first found.
     */
    private void checkTilesForHoeing() {

    }

    private void tryCreatePlantingTasks() {
        if(months.contains(GameMvc.getInstance().getModel().get(GameCalendar.class).getMonth())) {

        }
    }

    /**
     * Updates set of months, to have only months, where plants should be planted.
     */
    public void updateMonths() {
        months.clear();
        PlantMap plantMap = PlantMap.getInstance();
        for(String plant : plants) {
            PlantType type = plantMap.getPlantType(plant);
            if(type.getPlantingStart() != null) {
                months.addAll(type.getPlantingStart());
            }
        }
    }

    /**
     * Checks plants inside farm, creates tasks.
     */
    private void checkPlants() {

    }

    /**
     * Creates {@link Designation} based on given task and adds it to container.
     */
    private void submitTaskToContainer(Task task) {
        GameMvc.getInstance().getModel().get(TaskContainer.class);
    }

    public Set<String> getPlants() {
        return plants;
    }
}
