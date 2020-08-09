package stonering.game.model.system.unit;

import stonering.entity.job.Task;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.needs.NeedAspect;
import stonering.entity.unit.aspects.TaskAspect;
import stonering.entity.unit.aspects.needs.NeedEnum;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.EntitySystem;
import stonering.game.model.system.task.TaskContainer;
import stonering.util.global.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static stonering.enums.action.TaskPriorityEnum.NONE;
import static stonering.enums.action.TaskStatusEnum.COMPLETE;
import static stonering.enums.action.TaskStatusEnum.FAILED;

/**
 * System for generation need satisfying tasks for units. Works in {@link UnitContainer}.
 * On update, counts creature needs and creates {@link Task} for strongest need in {@link NeedAspect}.
 * This task is stored in needs aspect (not in {@link TaskContainer}) and considered by {@link CreaturePlanningSystem}.
 *
 * @author Alexander on 22.08.2019.
 */
public class NeedTaskSystem extends EntitySystem<Unit> {

    public NeedTaskSystem() {
        updateInterval = TimeUnitEnum.MINUTE;
        targetAspects.add(NeedAspect.class);
    }

    /**
     * Checks if current needs task is ended, and creates a new one if needed.
     */
    @Override
    public void update(Unit unit) {
        unit.getOptional(NeedAspect.class)
                .filter(this::isTaskEnded)
                .ifPresent(aspect -> tryAssignNewTask(unit, aspect));
    }

    private boolean isTaskEnded(NeedAspect aspect) {
        return aspect.satisfyingTask == null || aspect.satisfyingTask.status == FAILED || aspect.satisfyingTask.status == COMPLETE; 
    }
    
    /**
     * Fetches untolerated needs in the order of their priority, tries to create task for satisfaction.
     * First successfully created task is saved to aspect (and then considered in {@link TaskAspect}).
     */
    private void tryAssignNewTask(Unit unit, NeedAspect aspect) {
        aspect.satisfyingTask = null;
        for (Pair<NeedEnum, Integer> need : getUntoleratedNeeds(unit, aspect)) {
            aspect.satisfyingTask = need.getKey().NEED.tryCreateTask(unit);
            if (aspect.satisfyingTask != null) return;
        }
    }

    private List<Pair<NeedEnum, Integer>> getUntoleratedNeeds(Unit unit, NeedAspect aspect) {
        return aspect.needs.stream()
                .map(need -> new Pair<>(need, need.NEED.countPriority(unit).VALUE)) // count priority
                .filter(pair -> pair.value > NONE.VALUE) // filter tolerated needs
                .sorted(Comparator.comparingInt(pair -> ((Pair<NeedEnum, Integer>) pair).value).reversed())
                .collect(Collectors.toList());
    }
}
