package stonering.entity.unit.aspects.needs;

import stonering.entity.Entity;
import stonering.entity.building.Building;
import stonering.entity.building.aspects.RestFurnitureAspect;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.SleepInBedAction;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.health.HealthParameterState;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.action.TaskPriorityEnum;
import stonering.enums.unit.health.FatigueParameter;
import stonering.enums.unit.health.HealthParameterEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.unit.CreatureHealthSystem;
import stonering.util.geometry.Position;

import java.util.List;
import java.util.Optional;

/**
 * Need for rest. Part of {@link CreatureHealthSystem}.
 * Uses {@link FatigueParameter} and {@link HealthParameterState} in {@link HealthAspect} for calculating task priority.
 * Generates tasks for:
 * stop activities on medium exhaustion,
 * sleeping in a bed or a safe place (50-70),
 * sleeping at safe place (70-90),
 * sleeping at any place (>90).
 *
 * TODO add 'function' for getting suitable place to sleep: warm > cold, inside > outside, own > public.
 * TODO night shift
 *
 * @author Alexander on 22.08.2019.
 */
public class RestNeed extends Need {
    @Override
    public TaskPriorityEnum countPriority(Entity entity) {
        HealthAspect health = entity.getAspect(HealthAspect.class);
        float relativeFatigue = health.parameters.get(HealthParameterEnum.FATIGUE).getRelativeValue();
        return HealthParameterEnum.FATIGUE.PARAMETER.getRange(relativeFatigue).priority;
    }

    /**
     * Returns null, if no bed available on medium exhaustion, Returns task to sleep on the floor on strong exhaustion.
     */
    @Override
    public Task tryCreateTask(Entity entity) {
        TaskPriorityEnum priority = countPriority(entity);
        switch (priority) {
            case NONE:
                break;
            case COMFORT:
            case JOB:
            case HEALTH_NEEDS: // sleep in bed
            case SAFETY:
                //TODO sleep at safe place (at home, under roof)
            case LIFE:
                //TODO fall asleep at current place
                return selectBuildingToSleep(entity.position).map(building -> createTaskToSleep(building, priority)).orElse(null);
        }
        return null;
    }

    private Optional<Building> selectBuildingToSleep(Position position) {
        List<Building> buildings = GameMvc.model().get(BuildingContainer.class).getBuildingsWithAspect(RestFurnitureAspect.class);
        buildings = GameMvc.model().get(LocalMap.class).passageMap.util.filterEntitiesByReachability(buildings, position);
        return Optional.of(buildings.isEmpty() ? null : buildings.get(0));
    }

    private Task createTaskToSleep(Building building, TaskPriorityEnum priority) {
        Action restAction = new SleepInBedAction(new EntityActionTarget(building, ActionTargetTypeEnum.EXACT));
        return new Task("sleep", restAction, priority.VALUE);
    }
}
