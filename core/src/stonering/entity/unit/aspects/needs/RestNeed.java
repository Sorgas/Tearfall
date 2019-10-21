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
import stonering.enums.unit.health.HealthParameterEnum;
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
    @Override
    public TaskPrioritiesEnum countPriority(Entity entity) {
        HealthAspect aspect = entity.getAspect(HealthAspect.class);
        return HealthParameterEnum.FATIGUE.PARAMETER.priorities[getExhaustionLevel(aspect)];
    }

    /**
     * Returns null, if no bed available on medium exhaustion, Returns task to sleep on the floor on strong exhaustion.
     */
    @Override
    public Task tryCreateTask(Entity entity) {
        TaskPrioritiesEnum priority = countPriority(entity);
        switch (priority) {
            case NONE:
                break;
            case COMFORT:
            case JOB:
            case HEALTH_NEEDS: // sleep in bed
            case SAFETY:
                //TODO sleep at safe place (at home, under roof)f
            case LIFE:
                //TODO fall asleep at current place
                List<Building> buildings = GameMvc.instance().getModel().get(BuildingContainer.class).getBuildingsWithAspect(RestFurnitureAspect.class);
                buildings = GameMvc.instance().getModel().get(LocalMap.class).getPassage().filterEntitiesByReachability(buildings, entity.position);
                if (!buildings.isEmpty()) { // bed available, no sleep without bed at this level of exhaustion
                    Action restAction = new RestAction(new EntityActionTarget(buildings.get(0), ActionTarget.EXACT));
                    return new Task("sleep", restAction, priority.VALUE);
                }
                break;
        }
        return null;
    }

    /**
     * Counts creature's relative fatigue, and determines level of exhaustion.
     * Used for defining place to sleep and rest task priority.
     */
    private int getExhaustionLevel(HealthAspect aspect) {
        float relativeFatigue = aspect.parameters.get(HealthParameterEnum.FATIGUE).getRelativeValue();
        //TODO add day/night state to relativeFatigue
        return HealthParameterEnum.FATIGUE.PARAMETER.getRangeIndex(relativeFatigue);
    }
}
