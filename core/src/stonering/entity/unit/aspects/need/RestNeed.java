package stonering.entity.unit.aspects.need;

import static stonering.enums.action.TaskPriorityEnum.*;
import static stonering.enums.unit.health.OldNeedEnum.*;

import stonering.entity.building.Building;
import stonering.entity.building.aspects.RestFurnitureAspect;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.SleepInBedAction;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MoodEffect;
import stonering.entity.unit.aspects.body.DiseaseState;
import stonering.entity.unit.aspects.health.HealthAspect;
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
        float fatigue = fatigueLevel(unit);
        if(fatigue < 0.5f) return NONE;
        if(fatigue < 0.7f) return JOB;
        if(fatigue < 0.9f) return HEALTH_NEEDS;
        return SAFETY;
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
            case JOB:
                // sleep in bed
            case HEALTH_NEEDS: 
                //TODO sleep at home only
            case SAFETY:
                //TODO sleep in any bed
                //TODO fall asleep at current place
                return selectBuildingToSleep(unit.position)
                        .map(SleepInBedAction::new)
                        .map(action -> new Task(action, priority.VALUE))
                        .orElse(null);
        }
        return null;
    }

    @Override
    public DiseaseState createDisease() {
        // create insomnia disease
        
        return null;
    }

    @Override
    public MoodEffect getMoodPenalty(Unit unit, NeedState state) {
        
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
    
    private float fatigueLevel(Unit unit) {
        return unit.get(NeedAspect.class).needs.get(NeedEnum.REST).getRelativeValue();
    }
}
