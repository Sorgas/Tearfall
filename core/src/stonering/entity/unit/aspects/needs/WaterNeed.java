package stonering.entity.unit.aspects.needs;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.job.Task;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.action.TaskPriorityEnum;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.unit.health.HealthParameterEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;

/**
 * @author Alexander on 08.10.2019.
 */
public class WaterNeed extends Need {

    @Override
    public TaskPriorityEnum countPriority(Entity entity) {
        HealthAspect aspect = entity.get(HealthAspect.class);
        float relativeValue = aspect.parameters.get(HealthParameterEnum.THIRST).getRelativeValue();
        return HealthParameterEnum.THIRST.PARAMETER.getRange(relativeValue).priority;
    }

    @Override
    public Task tryCreateTask(Entity entity) {
        TaskPriorityEnum priority = countPriority(entity);
        Item item = findBestDrink(entity);
        if(item == null) {
            
            //TODO drink from water sources
        }
        switch(priority) {
            case COMFORT:
                break;
            case JOB:
                break;
            case HEALTH_NEEDS:
                break;
            case SAFETY:
                break;
            case LIFE:
                break;
        }
        return null;
    }

    private Item findBestDrink(Entity entity) {
        return GameMvc.model().get(ItemContainer.class).util.getNearestItemWithTag(entity.position, ItemTagEnum.DRINKABLE);
    }
}
