package stonering.entity.local.zone;


import stonering.designations.Designation;
import stonering.entity.jobs.Task;
import stonering.entity.local.environment.GameCalendar;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.PlantType;
import stonering.game.core.GameMvc;
import stonering.game.core.model.lists.ItemContainer;
import stonering.game.core.model.lists.TaskContainer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Farm keeps track on plants condition in its zone, and create tasks respectively.
 * Farm have list of enabled plants and list of months,
 *
 * @author Alexander on 04.03.2019.
 */
public class FarmZoneActor extends ZoneActor {
    private Set<String> plants; // plants selected for growing.
    private Set<Integer> months; // planting tasks are created only in these months
    private ItemSelector seedSelector; // planting tasks share this selector. it is updated as months change.

    public FarmZoneActor(String name, Zone zone) {
        super(name, zone);
        plants = new HashSet<>();
        months = new HashSet<>();
    }

    @Override
    public void turn() {

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
