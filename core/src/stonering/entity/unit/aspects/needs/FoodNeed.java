package stonering.entity.unit.aspects.needs;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.EatAction;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.action.TaskPrioritiesEnum;
import stonering.enums.items.TagEnum;
import stonering.enums.unit.health.HealthParameterEnum;
import stonering.enums.unit.health.HungerParameter;
import stonering.game.model.system.unit.CreatureHealthSystem;

/**
 * Need for eating. Part of {@link CreatureHealthSystem}.
 * See also {@link HungerParameter}.
 *
 * @author Alexander on 30.09.2019.
 */
public class FoodNeed extends Need {

    @Override
    public TaskPrioritiesEnum countPriority(Entity entity) {
        HealthAspect aspect = entity.getAspect(HealthAspect.class);
        return HealthParameterEnum.HUNGER.PARAMETER.priorities[getHungerLevel(aspect)];
    }

    @Override
    public Task tryCreateTask(Entity entity) {
        TaskPrioritiesEnum priority = countPriority(entity);
        Item item = getBestAvailableFood();
        if (item == null) return null;
        switch(priority) {
            case NONE:
                break;
            case COMFORT:
            case HEALTH_NEEDS: // eat prepared items and raw vegetables
            case SAFETY: // eat raw items
            case LIFE: // eat spoiled items
                if (item.tags.contains(TagEnum.SPOILED) || item.tags.contains(TagEnum.RAW)) return null;
                Action eatAction = new EatAction(item);
                return new Task("eat", eatAction, priority.VALUE);
        }
        return null;
    }

    /**
     * Selects prepared food, then not prepared, then raw, then spoiled variants in same order.
     * Bad decreases task priority. Substracted from hunger level, this will make units refuse to eat bad food even being very hungry.
     */
    private Item getBestAvailableFood() {
        return null;
    }

    private int getHungerLevel(HealthAspect aspect) {
        float relativeHunger = aspect.parameters.get(HealthParameterEnum.HUNGER).getRelativeValue();
        return HealthParameterEnum.HUNGER.PARAMETER.getRangeIndex(relativeHunger);
    }
}
