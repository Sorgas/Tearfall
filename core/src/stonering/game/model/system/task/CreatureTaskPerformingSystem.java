package stonering.game.model.system.task;

import stonering.entity.FloatPositionEntity;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.target.ActionTargetStatusEnum;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.game.model.system.EntitySystem;

import static stonering.entity.job.action.target.ActionTargetStatusEnum.*;
import static stonering.enums.action.TaskStatusEnum.*;

/**
 * System for performing tasks of units. Call perform() of task's actions, and updates task statuses.
 *
 * @author Alexander on 29.10.2019.
 */
public class CreatureTaskPerformingSystem extends EntitySystem<Unit> {

    /**
     * Updates state of given unit's active task. Selects new task, fails and finishes current.
     * Tasks logic is invoked from here.
     */
    @Override
    public void update(Unit unit) {
        PlanningAspect aspect = unit.getAspect(PlanningAspect.class);
        if (aspect == null) return;
        Task task = aspect.task;
        if (task != null && task.status == ACTIVE) checkTarget(aspect, task);
    }

    /**
     * Checks if unit is in position for performing action, handles different cases of positioning.
     */
    private void checkTarget(PlanningAspect aspect, Task task) {
        if(aspect.getEntity() instanceof FloatPositionEntity);
        ActionTargetStatusEnum checkResult = task.nextAction.actionTarget.check(aspect.getEntity().position);
        aspect.movementNeeded = checkResult == WAIT;
        switch (checkResult) {
            case READY:
                handleReachingTarget(task, aspect);
                break;
            case NEW: // will be handled on next update
            case WAIT: // no handle required
                break;
            case FAIL:
                task.status = FAILED;
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
                return;
            case Action.FAIL:
                task.status = FAILED;
                aspect.actionChecked = false; // task will be removed
        }
    }
}
