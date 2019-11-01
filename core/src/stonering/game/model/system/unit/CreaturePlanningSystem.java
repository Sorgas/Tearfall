package stonering.game.model.system.unit;

import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.entity.unit.aspects.needs.NeedsAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.task.CreatureTaskPerformingSystem;
import stonering.game.model.system.task.TaskContainer;
import stonering.util.global.Logger;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import static stonering.enums.TaskStatusEnum.*;

/**
 * System for giving {@link Task}s to {@link Unit}s.
 * Looks for new task for unit if it hasn't one.
 * Removes finished, failed and paused tasks from units.
 * Considers only task statuses, see {@link CreatureTaskPerformingSystem}.
 * TODO add target aspects to systems
 *
 * @author Alexander on 28.10.2019.
 */
public class CreaturePlanningSystem {
    public UnitContainer container;

    public void update(Unit unit) {
        PlanningAspect aspect = unit.getAspect(PlanningAspect.class);
        if (aspect == null) return;
        if (aspect.task == null) {
            findAndAssignNewTask(unit);
        } else {
            checkTaskStatus(aspect, aspect.task);
        }
    }

    private void checkTaskStatus(@NotNull PlanningAspect aspect, @NotNull Task task) {
        switch (task.status) {
            case OPEN:
                task.status = ACTIVE; // start claimed and open task
                break;
            case FAILED:
            case COMPLETE:
                freeAspect(aspect);
                task.reset();
        }
    }

    private void findAndAssignNewTask(Unit unit) {
        Logger.TASKS.logDebug("Selecting task for " + unit);
        Task task = selectTaskForUnit(unit);
        if (task != null && unitCanPerformTask(unit, task)) assignTaskToUnit(unit, task); // try assign new task
    }

    /**
     * Finds appropriate task for unit.
     * Checks priorities of all available tasks.
     * TODO combat tasks
     * TODO non possible tasks with high priority can block other tasks
     */
    private Task selectTaskForUnit(Unit unit) {
        Logger.TASKS.logDebug("Looking for new task for unit " + unit);
        ArrayList<Task> tasks = new ArrayList<>();
        if (unit.hasAspect(NeedsAspect.class)) tasks.add(unit.getAspect(NeedsAspect.class).satisfyingTask);
        tasks.add(taskContainer().getActiveTask(unit)); // player/workbench created tasks
        return tasks.stream().filter(Objects::nonNull).max(Comparator.comparingInt(task1 -> task1.priority)).orElse(null);
    }

    /**
     * Sets tasks to aspect. Task should be checked, or will fail short after assignment.
     */
    private void assignTaskToUnit(@NotNull Unit unit, @NotNull Task task) {
        Logger.TASKS.logDebug("Assigning task " + task + " to unit " + unit);
        taskContainer().claimTask(task);
        unit.getAspect(PlanningAspect.class).task = task;
        task.status = OPEN;
    }

    private void freeAspect(@NotNull PlanningAspect aspect) {
        aspect.task = null; // free this aspect
        aspect.movementNeeded = false;
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
        int result;
        while ((result = task.nextAction.check()) == Action.NEW) {
        } // can create sub actions
        if(result == Action.OK) return true;
        task.reset();
        return false;
    }

    /**
     * Completely removes task from game. Used for finished and failed tasks. Repeated tasks in workbenches are handled separately.
     * Task should be assigned in {@link TaskContainer} ans have performer.
     */
    private void removeTask(Task task) {
        taskContainer().removeTask(task);
        GameMvc.instance().getModel().get(ItemContainer.class).freeItems(task.lockedItems); // free items
    }

    private TaskContainer taskContainer() {
        return GameMvc.instance().getModel().get(TaskContainer.class);
    }

    /**
     * For cancelling task, caused by external factor (path blocking, enemy, player).
     */
    public void interrupt(PlanningAspect aspect) {
        if (aspect.task == null) return;
        Logger.TASKS.logDebug("Resetting planning aspect of " + toString());
        Task task = aspect.task;

//        assignTaskToUnit(null);
        task.reset();
    }
}
