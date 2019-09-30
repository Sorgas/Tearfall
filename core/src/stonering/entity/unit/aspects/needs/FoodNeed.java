package stonering.entity.unit.aspects.needs;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;

/**
 *
 *
 * @author Alexander on 30.09.2019.
 */
public class FoodNeed extends Need {

    @Override
    public int countPriority(Entity entity) {
        return 0;
    }

    @Override
    public Task tryCreateTask(Entity entity) {
        Action
        Task task = new Task();

    }

    /**
     * Selects prepared food, then not prepared, then raw, then spoiled variants in same order.
     * Bad decreases task priority. Substracted from hunger level, this will make units refuse to eat bad food even being very hungry.
     */
    private Item getBestAvailableFood() {

    }
}
