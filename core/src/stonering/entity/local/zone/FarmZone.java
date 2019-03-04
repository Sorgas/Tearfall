package stonering.entity.local.zone;


import stonering.designations.Designation;
import stonering.entity.jobs.Task;
import stonering.game.core.GameMvc;
import stonering.game.core.model.lists.TaskContainer;

/**
 * Farm keeps track on plants condition in its zone, and create tasks respectively.
 *
 * @author Alexander on 04.03.2019.
 */
public class FarmZone extends ZoneActor {

    @Override
    public void turn() {

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
}
