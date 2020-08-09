package stonering.entity.unit.aspects.needs;

import static stonering.enums.unit.health.NeedEnum.*;

import stonering.entity.building.Building;
import stonering.entity.building.aspects.RestFurnitureAspect;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.SleepInBedAction;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.health.NeedState;
import stonering.enums.action.TaskPriorityEnum;
import stonering.enums.unit.health.FatigueParameter;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.unit.CreatureHealthSystem;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

import java.util.Comparator;
import java.util.Optional;

/**
 * Need for rest. Part of {@link CreatureHealthSystem}.
 * Uses {@link FatigueParameter} and {@link NeedState} in {@link HealthAspect} for calculating task priority.
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
    public TaskPriorityEnum countPriority(Unit unit) {
        return Optional.ofNullable(unit.get(NeedAspect.class))
                .map(aspect -> aspect.needs.get(FATIGUE).getRelativeValue())
                .map(relativeFatigue -> FATIGUE.PARAMETER.getRange(relativeFatigue).priority)
                .orElse(TaskPriorityEnum.NONE);
    }

    /**
     * Returns null, if no bed available on medium exhaustion, Returns task to sleep on the floor on strong exhaustion.
     */
    @Override
    public Task tryCreateTask(Unit unit) {
        TaskPriorityEnum priority = countPriority(unit);
        switch (priority) {
            case NONE:
                return Logger.NEED.logWarn("Attempt to create sleep task with none priority", null);
            case COMFORT:
            case JOB:
            case HEALTH_NEEDS: // sleep in bed
                //TODO sleep at home only
            case SAFETY:
                //TODO sleep in any bed
            case LIFE:
                //TODO fall asleep at current place
                return selectBuildingToSleep(unit.position)
                        .map(SleepInBedAction::new)
                        .map(action -> new Task(action, priority.VALUE))
                        .orElse(null);
        }
        return null;
    }

    private Optional<Building> selectBuildingToSleep(Position position) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        return GameMvc.model().get(BuildingContainer.class).stream()
                .filter(building -> building.position != null)
                .filter(building -> building.has(RestFurnitureAspect.class))
                .filter(building -> map.passageMap.inSameArea(building.position, position))
                .min(Comparator.comparingInt(building -> position.fastDistance(building.position)));
    }

    private Task createTaskToSleep(Building building, TaskPriorityEnum priority) {
        Action restAction = new SleepInBedAction(building);
        return new Task(restAction, priority.VALUE);
    }
}
