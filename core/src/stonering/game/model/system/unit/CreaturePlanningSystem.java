package stonering.game.model.system.unit;

import stonering.entity.job.Task;
import stonering.entity.job.action.ActionConditionStatusEnum;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.entity.unit.aspects.needs.NeedsAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.EntitySystem;
import stonering.game.model.system.task.CreatureActionPerformingSystem;
import stonering.game.model.system.task.TaskContainer;
import stonering.util.global.Logger;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;
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
        if (unit.getAspect(PlanningAspect.class).task == null) {
            findNewTask(unit);
        } else {
            checkTaskStatus(unit);
        }
    }

    private void checkTaskStatus(Unit unit) {
        PlanningAspect planning = unit.getAspect(PlanningAspect.class);
        Task task = planning.task;
        switch (task.status) {
            case OPEN:
                Logger.TASKS.logWarn("claimed task with open status");
                task.status = ACTIVE; // start claimed and open task
                break;
            case FAILED:
            case COMPLETE:
            case CANCELED:
                unit.getAspect(MovementAspect.class).reset();
                planning.task = null; // free this aspect
                task.reset();
        }
    }

    private void findNewTask(Unit unit) {
        Task task = selectTaskForUnit(unit);
        if (task == null) return;
        task.performer = unit;
        if (task.initialAction.takingCondition.get()) {
            Logger.TASKS.logDebug("Assigning task " + task + " to unit " + unit);
            taskContainer().claimTask(task);
            unit.getAspect(PlanningAspect.class).task = task;
            task.status = ACTIVE;
        }
    }

    /**
     * Finds appropriate task for unit.
     * Checks priorities of all available tasks.
     * TODO combat tasks
     * TODO non possible tasks with high priority can block other tasks
     */
    private Task selectTaskForUnit(Unit unit) {
        ArrayList<Task> tasks = new ArrayList<>();
        if (unit.hasAspect(NeedsAspect.class)) tasks.add(unit.getAspect(NeedsAspect.class).satisfyingTask); // add need task
        tasks.add(taskContainer().getActiveTask(unit)); // get task from container
        return tasks.stream()
                .filter(Objects::nonNull)
                .filter(task -> task.status == OPEN)
                .max(Comparator.comparingInt(task1 -> task1.priority)).orElse(null);
    }

    /**
     * Checks if task (with all sub-actions) can be performed by unit.
     * In this method requirement aspects of actions create additional actions.
     *
     * @return false, if some action in sequence cannot be performed.
     */
    private boolean unitCanPerformTask(@NotNull Unit unit, @NotNull Task task) {
        Logger.TASKS.logDebug("Checking task " + task + " for unit " + unit);
        task.performer = unit; // performer is required for checking
        ActionConditionStatusEnum result;
        while ((result = task.nextAction.startCondition.get()) == NEW) {
        } // can create sub actions
        if (result == OK) return true;
        task.reset();
        return false;
    }

    private TaskContainer taskContainer() {
        return GameMvc.model().get(TaskContainer.class);
    }
}
