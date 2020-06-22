package stonering.game.model.system.unit;

import stonering.entity.job.Task;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.TaskAspect;
import stonering.entity.unit.aspects.needs.NeedsAspect;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.EntitySystem;
import stonering.game.model.system.task.CreatureActionPerformingSystem;
import stonering.game.model.system.task.TaskContainer;
import stonering.util.geometry.Position;
import stonering.util.geometry.PositionUtil;
import stonering.util.logging.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import static stonering.enums.action.TaskStatusEnum.*;

/**
 * System for giving {@link Task}s to {@link Unit}s.
 * Looks for new task for unit if it hasn't one.
 * Removes finished, failed and paused tasks from units.
 * Considers only task statuses, see {@link CreatureActionPerformingSystem}.
 *
 * @author Alexander on 28.10.2019.
 */
public class CreaturePlanningSystem extends EntitySystem<Unit> {
    public UnitContainer container;

    public CreaturePlanningSystem() {
        targetAspects.add(TaskAspect.class);
    }

    @Override
    public void update(Unit unit) {
        if (unit.get(TaskAspect.class).task == null) {
            findNewTask(unit);
        } else {
            checkTaskStatus(unit);
        }
    }

    private void checkTaskStatus(Unit unit) {
        TaskAspect taskAspect = unit.get(TaskAspect.class);
        Task task = taskAspect.task;
        switch (task.status) {
            case OPEN: // invalid case
                Logger.TASKS.logWarn("claimed task with open status");
                task.status = ACTIVE; // start claimed and open task
                break;
            case FAILED:
            case COMPLETE:
            case CANCELED:
                unit.get(MovementAspect.class).reset();
                taskAspect.task = null; // free this aspect
                task.reset();
        }
    }

    private void findNewTask(Unit unit) {
        Optional.ofNullable(selectTaskForUnit(unit))
                .filter(task -> checkTaskForUnit(task, unit))
                .ifPresent(task -> {
                    Logger.TASKS.logDebug("Assigning task " + task + " to unit " + unit);
                    taskContainer().claimTask(task);
                    unit.get(TaskAspect.class).task = task;
                    task.status = ACTIVE;
                });
    }
    
    /**
     * Finds appropriate task for unit.
     * Checks priorities of all available tasks.
     * TODO combat tasks
     * TODO non possible tasks with high priority can block other tasks
     */
    private Task selectTaskForUnit(Unit unit) {
        ArrayList<Task> tasks = new ArrayList<>();
        if (unit.has(NeedsAspect.class)) tasks.add(unit.get(NeedsAspect.class).satisfyingTask); // add need task
        tasks.add(taskContainer().getActiveTask(unit)); // get task from container
        LocalMap map = GameMvc.model().get(LocalMap.class);
        return tasks.stream()
                .filter(Objects::nonNull)
                .filter(task -> task.status == OPEN)
                .filter(task -> {
                    if(task.designation == null) return true;
                    return PositionUtil.allNeighbourDeltas.stream()
                            .map(pos -> Position.add(pos, task.designation.position))
                            .map(pos -> map.passageMap.area.get(task.designation.position))
                            .anyMatch(area -> area == map.passageMap.area.get(unit.position));
                })
                .max(Comparator.comparingInt(task1 -> task1.priority)).orElse(null);
    }

    private boolean checkTaskForUnit(Task task, Unit unit) {
        task.performer = unit;
        if (!task.initialAction.takingCondition.get()) {
            task.reset();
            return false;
        }
        return true;
    }
    
    private TaskContainer taskContainer() {
        return GameMvc.model().get(TaskContainer.class);
    }
}
