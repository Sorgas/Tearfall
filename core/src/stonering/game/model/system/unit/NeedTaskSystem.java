package stonering.game.model.system.unit;

import stonering.entity.job.Task;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.entity.unit.aspects.TaskAspect;
import stonering.entity.unit.aspects.need.NeedEnum;
import stonering.entity.unit.aspects.need.NeedState;
import stonering.enums.action.TaskPriorityEnum;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.EntitySystem;
import stonering.game.model.system.task.TaskContainer;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static stonering.enums.action.TaskPriorityEnum.NONE;
import static stonering.enums.action.TaskStatusEnum.COMPLETE;
import static stonering.enums.action.TaskStatusEnum.FAILED;

/**
 * System for handling creatures needs.
 * On update, counts creature needs and creates {@link Task} for strongest need in {@link NeedAspect}.
 * This task is stored in needs aspect (not in {@link TaskContainer}) and considered by {@link CreaturePlanningSystem}.
 * If some need has reached max value, creates disease for it.
 *
 * @author Alexander on 22.08.2019.
 */
public class NeedTaskSystem extends EntitySystem<Unit> {
    public static float DEFAULT_DELTA = 1f / 16;

    public NeedTaskSystem() {
        updateInterval = TimeUnitEnum.MINUTE;
        targetAspects.add(NeedAspect.class);
    }

    /**
     * Checks if current needs task is ended, and creates a new one if needed.
     */
    @Override
    public void update(Unit unit) {
        unit.getOptional(NeedAspect.class).ifPresent(aspect -> {
            aspect.needs.values().forEach(state -> state.changeValue(DEFAULT_DELTA));
            if (aspect.satisfyingTask == null || aspect.satisfyingTask.status == FAILED || aspect.satisfyingTask.status == COMPLETE) {
                tryAssignNewTask(unit, aspect);
            }
        });
    }

    /**
     * Fetches untolerated needs in the order of their priority, tries to create task for satisfaction.
     * First successfully created task is saved to aspect (and then considered in {@link TaskAspect}).
     */
    private void tryAssignNewTask(Unit unit, NeedAspect aspect) {
        aspect.satisfyingTask = null;
        for (NeedState state : getUntoleratedNeeds(unit, aspect)) {
            aspect.satisfyingTask = state.need.NEED.tryCreateTask(unit);
            if (aspect.satisfyingTask != null) return;
        }
    }

    private List<NeedState> getUntoleratedNeeds(Unit unit, NeedAspect aspect) {
        return aspect.needs.values().stream()
                .filter(state -> state.current() > NONE.NEED_THRESHOLD) // filter tolerated needs
                .sorted(Comparator.comparingDouble(state -> ((NeedState) state).current()).reversed())
                .collect(Collectors.toList());
    }
}
