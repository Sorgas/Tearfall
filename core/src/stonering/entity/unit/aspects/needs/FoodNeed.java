package stonering.entity.unit.aspects.needs;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.EatAction;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.action.TaskPriorityEnum;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.unit.health.HealthParameterEnum;
import stonering.enums.unit.health.HungerParameter;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.item.ItemsStream;
import stonering.game.model.system.unit.CreatureHealthSystem;

import java.util.HashMap;
import java.util.Map;

/**
 * Need for eating. Part of {@link CreatureHealthSystem}.
 * See also {@link HungerParameter}.
 *
 * @author Alexander on 30.09.2019.
 */
public class FoodNeed extends Need {
    private Map<ItemTagEnum, Integer> itemsSelectionPriority = new HashMap<>();

    {

    }

    @Override
    public TaskPriorityEnum countPriority(Entity entity) {
        HealthAspect aspect = entity.getAspect(HealthAspect.class);
        float relativeHunger = aspect.parameters.get(HealthParameterEnum.HUNGER).getRelativeValue();
        return HealthParameterEnum.HUNGER.PARAMETER.getRange(relativeHunger).priority;
    }

    @Override
    public Task tryCreateTask(Entity entity) {
        TaskPriorityEnum priority = countPriority(entity);
        switch(priority) {
            case NONE:
                break;
            case COMFORT:
            case HEALTH_NEEDS: // eat prepared items and raw vegetables
            case SAFETY: // eat raw items

            case LIFE: // eat spoiled items
                Item item = getBestAvailableFood();
                if (item == null) return null;
                if (item.tags.contains(ItemTagEnum.SPOILED) || item.tags.contains(ItemTagEnum.RAW)) return null;
                Action eatAction = new EatAction(item);
                return new Task("eat", eatAction, priority.VALUE);
        }
        return null;
    }

    /**
     * Selects prepared food, then not prepared, then raw, then spoiled variants in same order.
     * Bad decreases task priority. Substracted from hunger level, this will make units refuse to eat bad food even being very hungry.
     */
    private Item getBestAvailableFood(Entity entity) {
        new ItemsStream().filterHasTag(ItemTagEnum.EDIBLE).filterByReachability(entity.position).getNearestTo(entity.position);
        ItemContainer container = GameMvc.model().get(ItemContainer.class).
        return null; // TODO
    }

     private Item selectPreparedFood() {

     }
}
