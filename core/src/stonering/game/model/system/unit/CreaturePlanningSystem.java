package stonering.game.model.system.unit;

import stonering.entity.job.Task;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.entity.unit.aspects.needs.NeedsAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.EntitySystem;
import stonering.game.model.system.task.CreatureActionPerformingSystem;
import stonering.game.model.system.task.TaskContainer;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

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
        targetAspects.add(PlanningAspect.class);
    }

    @Override
    public void update(Unit unit) {
        if (unit.get(PlanningAspect.class).task == null) {
            findNewTask(unit);
        } else {
            checkTaskStatus(unit);
        }
    }

    private void checkTaskStatus(Unit unit) {
        PlanningAspect planning = unit.get(PlanningAspect.class);
        Task task = planning.task;
        switch (task.status) {
            case OPEN:
                Logger.TASKS.logWarn("claimed task with open status");
                task.status = ACTIVE; // start claimed and open task
                break;
            case FAILED:
            case COMPLETE:
            case CANCELED:
                unit.get(MovementAspect.class).reset();
                planning.task = null; // free this aspect
                task.reset();
        }
    }

    private void findNewTask(Unit unit) {
        Task task = selectTaskForUnit(unit);
        if (task == null) return;
        task.performer = unit;
        if (!task.initialAction.takingCondition.get()) return;
        Logger.TASKS.logDebug("Assigning task " + task + " to unit " + unit);
        taskContainer().claimTask(task);
        unit.get(PlanningAspect.class).task = task;
        task.status = ACTIVE;
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
        return tasks.stream()
                .filter(Objects::nonNull)
                .filter(task -> task.status == OPEN)
                .max(Comparator.comparingInt(task1 -> task1.priority)).orElse(null);
    }

    private TaskContainer taskContainer() {
        return GameMvc.model().get(TaskContainer.class);
    }
}
