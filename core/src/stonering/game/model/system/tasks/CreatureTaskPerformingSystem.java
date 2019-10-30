package stonering.game.model.system.tasks;

import stonering.entity.job.ItemOrderTask;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.target.ActionTargetStatusEnum;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.enums.TaskStatusEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.units.UnitContainer;

import static stonering.entity.job.action.target.ActionTargetStatusEnum.*;
import static stonering.enums.TaskStatusEnum.*;

/**
 * System for performing tasks of units. Call perform() of task's actions, and updates task statuses.
 *
 * @author Alexander on 29.10.2019.
 */
public class CreatureTaskPerformingSystem {

    /**
     * Updates state of given unit's active task. Selects new task, fails and finishes current.
     * Tasks logic is invoked from here.
     */
    public void update(Unit unit) {
        PlanningAspect aspect = unit.getAspect(PlanningAspect.class);
        if (aspect == null) return;
        Task task = aspect.task;
        if (task == null || task.status != ACTIVE) return;
        ActionTargetStatusEnum checkResult = task.nextAction.actionTarget.check(aspect.getEntity().position);
        aspect.movementNeeded = checkResult == WAIT;
        switch (checkResult) {
            case READY:
                handleReachingTarget(task, aspect);
                break;
            case WAIT: // no handle required
                break;
            case NEW: // will be handled on next update
                break;
            case FAIL:
                failTask(task);
                break;
        }
    }

    /**
     * Does logic when unit has reached action target.
     * {@link PlanningAspect} has flag that indicates whether current action has been checked.
     * Action is checked before performing, and right after previous action is finished.
     * Result of after check is not saved to repeat check when unit reaches target of new action.
     */
    private void handleReachingTarget(Task task, PlanningAspect aspect) {
        if (!aspect.actionChecked) {
            checkAction(task, aspect); // check before performing
            return;
        }
        if (task.nextAction.perform()) {
            if (task.isFinished()) { // was last action in task
                task.status = COMPLETE;
            } else {
                checkAction(task, aspect); // check next action after completion previous
                aspect.actionChecked = false; // action will be checked again, when unit reaches target
            }
        }
    }

    /**
     * Checks current action of task. Updates aspect flag of aspect and can fail task.
     */
    private void checkAction(Task task, PlanningAspect aspect) {
        switch (task.nextAction.check()) {
            case Action.OK:
                aspect.actionChecked = true;
                return;
            case Action.NEW:
                aspect.actionChecked = false; // new action is not yet checked
            case Action.FAIL:
                failTask(task);
                aspect.actionChecked = false; // new action is not yet checked
        }
    }

    public void failTask(Task task) {
        task.status = FAILED;
    }

    private void endTask(Task task, TaskStatusEnum status) {
        GameMvc.instance().getModel().get(UnitContainer.class).planningSystem.removeTask(task);
        if (task instanceof ItemOrderTask) ((ItemOrderTask) task).order.status = status;
    }
}
