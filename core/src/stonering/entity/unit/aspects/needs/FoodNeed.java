package stonering.entity.unit.aspects.needs;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.EatAction;
import stonering.entity.job.action.TaskTypesEnum;

/**
 *
 *
 * @author Alexander on 30.09.2019.
 */
public class FoodNeed extends Need {

    @Override
    public int countPriority(Entity entity) {
        entity.getAspect()
    }

    @Override
    public Task tryCreateTask(Entity entity) {
        Item item = getBestAvailableFood();
        if (item == null) return null;
        Action eatAction = new EatAction(item);
        return new Task("Eat task", TaskTypesEnum.OTHER, eatAction, countPriority(entity));
    }

    /**
     * Selects prepared food, then not prepared, then raw, then spoiled variants in same order.
     * Bad decreases task priority. Substracted from hunger level, this will make units refuse to eat bad food even being very hungry.
     */
    private Item getBestAvailableFood() {
        return null;
    }
}
