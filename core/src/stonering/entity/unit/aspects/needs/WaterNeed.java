package stonering.entity.unit.aspects.needs;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.TaskPrioritiesEnum;
import stonering.enums.items.TagEnum;
import stonering.enums.unit.health.HealthParameterEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.ItemContainer;

/**
 * @author Alexander on 08.10.2019.
 */
public class WaterNeed extends Need {

    @Override
    public TaskPrioritiesEnum countPriority(Entity entity) {
        HealthAspect aspect = entity.getAspect(HealthAspect.class);
        return HealthParameterEnum.THIRST.PARAMETER.priorities[getThirstLevel(aspect)];
    }

    @Override
    public Task tryCreateTask(Entity entity) {
        TaskPrioritiesEnum priority = countPriority(entity);
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
        return GameMvc.instance().getModel().get(ItemContainer.class).getNearestItemWithTag(entity.position, TagEnum.DRINKABLE);
    }

    private int getThirstLevel(HealthAspect aspect) {
        float value = aspect.parameters.get(HealthParameterEnum.THIRST).getRelativeValue();
        return HealthParameterEnum.THIRST.PARAMETER.getRangeIndex(value);
    }
}
