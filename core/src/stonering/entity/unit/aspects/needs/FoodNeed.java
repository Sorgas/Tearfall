package stonering.entity.unit.aspects.needs;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.EatAction;
import stonering.entity.job.action.TaskTypesEnum;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.TaskPrioritiesEnum;
import stonering.enums.items.TagEnum;
import stonering.game.model.system.units.CreatureHealthSystem;

/**
 * Need for eating. Part of {@link CreatureHealthSystem}.
 *
 *
 * @author Alexander on 30.09.2019.
 */
public class FoodNeed extends Need {
    private static final int NONE = 0; // no tasks
    private static final int LIGHT = 1; // sleep if no job
    private static final int MEDIUM = 2; // stop job and sleep
    private static final int HEAVY = 3; // seek safe place and sleep
    private static final int DEADLY = 4; // fall asleep immediately

    @Override
    public TaskPrioritiesEnum countPriority(Entity entity) {
        HealthAspect aspect = entity.getAspect(HealthAspect.class);
        switch(getHungerLevel(aspect)) {
            case NONE:
                return TaskPrioritiesEnum.NONE;
            case LIGHT:
                return TaskPrioritiesEnum.COMFORT;
            case MEDIUM:
                return TaskPrioritiesEnum.HEALTH_NEEDS;
            case HEAVY:
                return TaskPrioritiesEnum.SAFETY;
            case DEADLY:
                return TaskPrioritiesEnum.LIFE;
        }
        return TaskPrioritiesEnum.NONE;
    }

    @Override
    public Task tryCreateTask(Entity entity) {
        HealthAspect aspect = entity.getAspect(HealthAspect.class);
        Item item = getBestAvailableFood();
        if (item == null) return null;
        switch (getHungerLevel(aspect)) {
            case NONE:
                return null;
            case LIGHT: {
                if(item.tags.contains(TagEnum.SPOILED) || item.tags.contains(TagEnum.RAW)) return null;
                Action eatAction = new EatAction(item);
                return new Task("eat", TaskTypesEnum.OTHER, eatAction, countPriority(entity).VALUE);
            }
            case MEDIUM:
                //TODO sleep at safe place (at home, under roof)
            case HEAVY:
                //TODO sleep at any place
            case DEADLY:
                //TODO sleep immediately
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
        float relativeHunger = aspect.hunger / aspect.maxHunger;
        if (relativeHunger < 0.5) return NONE;
        if (relativeHunger < 0.7) return LIGHT;
        if (relativeHunger < 0.9) return MEDIUM;
        if (relativeHunger < 1) return HEAVY;
        return DEADLY;
    }
}
