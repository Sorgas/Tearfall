package stonering.game.model.system.unit;

import stonering.entity.job.Task;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.job.TaskAspect;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.EntitySystem;
import stonering.game.model.system.task.CreatureActionPerformingSystem;
import stonering.game.model.system.task.TaskContainer;
import stonering.game.model.system.task.TaskStatusSystem;
import stonering.util.logging.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import static stonering.enums.action.TaskStatusEnum.*;

/**
 * System for giving {@link Task}s to {@link Unit}s.
 * Looks for new task for unit if it hasn't one.
 * If unit's task is not active, it is unset from unit. Need tasks then collected by gc, others are handled in {@link TaskStatusSystem}.
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
        unit.optional(TaskAspect.class)
                .map(aspect -> aspect.task)
                .ifPresentOrElse(task -> handleTaskStatus(unit), () -> findNewTask(unit));
    }

    private void handleTaskStatus(Unit unit) {
        if (unit.get(TaskAspect.class).task.status != ACTIVE) removeTaskFromUnit(unit); // unit stopped to perform task for any reason
    }

    private void findNewTask(Unit unit) {
        ArrayList<Task> tasks = new ArrayList<>();
        unit.optional(NeedAspect.class) // add need task
                .map(aspect -> aspect.satisfyingTask)
                .ifPresent(tasks::add);
        tasks.add(taskContainer().getActiveTask(unit)); // get task from container
        tasks.stream()
                .filter(Objects::nonNull)
                .filter(task -> task.status == OPEN)
                .max(Comparator.comparingInt(task1 -> task1.priority)) // need tasks may be more important
                .ifPresent(task -> {
                    Logger.TASKS.logDebug("Assigning task " + task + " to unit " + unit);
                    taskContainer().claimTask(task); // others should not take this task
                    task.performer = unit;
                    unit.get(TaskAspect.class).task = task;
                    task.status = ACTIVE;
                });
    }

    public void removeTaskFromUnit(Unit unit) {
        unit.get(MovementAspect.class).reset();
        unit.get(TaskAspect.class).task = null;
    }
    
    private TaskContainer taskContainer() {
        return GameMvc.model().get(TaskContainer.class);
    }
}
