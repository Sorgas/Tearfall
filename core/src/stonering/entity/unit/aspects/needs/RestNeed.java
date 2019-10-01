package stonering.entity.unit.aspects.needs;

import stonering.entity.Entity;
import stonering.entity.building.Building;
import stonering.entity.building.aspects.RestFurnitureAspect;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.RestAction;
import stonering.entity.job.action.TaskTypesEnum;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.TaskPrioritiesEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.units.CreatureHealthSystem;

import java.util.List;

/**
 * Need for rest. Part of {@link CreatureHealthSystem}.
 * Generates tasks for:
 * stop activities on medium exhaustion,
 * sleeping in a bed or a safe place (50-70),
 * sleeping at safe place (70-90),
 * sleeping at any place (>90).
 * <p>
 * TODO add 'function' for getting suitable place to sleep: warm > cold, inside > outside, own > public.
 * TODO night shift
 *
 * @author Alexander on 22.08.2019.
 */
public class RestNeed extends Need {
    private static final int NONE = 0; // no tasks
    private static final int LIGHT = 1; // sleep if no job
    private static final int MEDIUM = 2; // stop job and sleep
    private static final int HEAVY = 3; // seek safe place and sleep
    private static final int DEADLY = 4; // fall asleep immediately

    @Override
    public TaskPrioritiesEnum countPriority(Entity entity) {
        HealthAspect aspect = entity.getAspect(HealthAspect.class);
        float relativeFatigue = aspect.fatigue / aspect.maxFatigue;
        switch (getExhaustionLevel(aspect)) {
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
        return TaskPrioritiesEnum.NONE; // invalid
    }

    /**
     * Returns null, if no bed available on medium exhaustion, Returns task to sleep on the floor on strong exhaustion.
     */
    @Override
    public Task tryCreateTask(Entity entity) {
        HealthAspect aspect = entity.getAspect(HealthAspect.class);
        switch (getExhaustionLevel(aspect)) {
            case NONE:
                return null;
            case LIGHT:
            case MEDIUM:
                List<Building> buildings = GameMvc.instance().getModel().get(BuildingContainer.class).getBuildingsWithAspect(RestFurnitureAspect.class);
                buildings = GameMvc.instance().getModel().get(LocalMap.class).getPassage().filterEntitiesByReachability(buildings, entity.position);
                if (!buildings.isEmpty()) { // bed available, no sleep without bed at this level of exhaustion
                    Action restAction = new RestAction(new EntityActionTarget(buildings.get(0), ActionTarget.EXACT));
                    return new Task("sleep", TaskTypesEnum.OTHER, restAction, countPriority(entity).VALUE);
                }
                break;
            case HEAVY:
                //TODO sleep at safe place (at home, under roof)
            case DEADLY:
                //TODO fall asleep at current place
        }
        return null;
    }

    /**
     * Counts creature's relative fatigue, and determines level of exhaustion.
     * Used for defining place to sleep and rest task priority.
     */
    private int getExhaustionLevel(HealthAspect aspect) {
        float relativeFatigue = aspect.fatigue / aspect.maxFatigue;
        //TODO add day/night state to relativeFatigue (add substract )
        if (relativeFatigue < 0.5) return NONE;
        if (relativeFatigue < 0.7) return LIGHT;
        if (relativeFatigue < 0.9) return MEDIUM;
        if (relativeFatigue < 1) return HEAVY;
        return DEADLY;
    }
}
