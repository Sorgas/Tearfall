package stonering.game.model.system.task;

import stonering.entity.job.Task;
import stonering.entity.job.action.ActionConditionStatusEnum;
import stonering.entity.job.action.target.ActionTargetStatusEnum;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.enums.action.ActionStatusEnum;
import stonering.game.model.system.EntitySystem;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;
import static stonering.enums.action.TaskStatusEnum.*;

/**
 * System for performing tasks of units. Only tasks with status ACTIVE handled here.
 * Algorithm:
 * 1. Task is active and unit is not moving
 * 2. Action target is checked
 * 3. Action is checked
 * 4. Unit moves to target(see {@link stonering.game.model.system.unit.CreatureMovementSystem})
 * 5. Action target is checked
 * 6. Action is checked
 * 7. Action is performed
 * <p>
 * Calls perform() of task's actions, and updates task statuses.
 * //TODO handle cases when creature cannon move. all actions with distant target are failed.
 *
 * @author Alexander on 29.10.2019.
 */
public class CreatureActionPerformingSystem extends EntitySystem<Unit> {

    public CreatureActionPerformingSystem() {
        targetAspects.add(PlanningAspect.class);
        targetAspects.add(MovementAspect.class);
    }

    @Override
    public void update(Unit unit) {
        PlanningAspect planning = unit.getAspect(PlanningAspect.class);
        MovementAspect movement = unit.getAspect(MovementAspect.class);
        Task task = planning.task;
        // creature has active task but is not moving
        if (task != null && task.status == ACTIVE && movement.target == null) checkTarget(planning, movement, task);
    }

    /**
     * Checks if unit is in position for performing action, handles different cases of positioning.
     * Creating new action during target check, will be handled on next update.
     */
    private void checkTarget(PlanningAspect planning, MovementAspect movement, Task task) {
        ActionTargetStatusEnum checkResult = task.nextAction.actionTarget.check(planning.entity);
        switch (checkResult) {
            case READY: // creature is in target, perform
                handleReachingActionTarget(task, planning);
                break;
            case WAIT: // unit is far from reachable target
                handleStartMovement(task, planning, movement);
                break;
            case FAIL: // target unreachable
                task.status = FAILED;
                break;
        }
    }

    /**
     * Does logic when unit has reached action target.
     * {@link PlanningAspect} has flag that indicates whether current action has been checked.
     * Action is checked before performing, and before starting moving to target.
     * Result of after check is not saved to repeat check when unit reaches target of new action.
     */
    private void handleReachingActionTarget(Task task, PlanningAspect aspect) {
        if (aspect.actionChecked) {
            task.nextAction.perform(); // perform action
            if (task.nextAction.status == ActionStatusEnum.COMPLETE && task.isNoActionsLeft())
                task.status = COMPLETE; // was last action in task
        } else {
            checkAction(task, aspect); // check before performing, can create new actions
        }
    }

    private void handleStartMovement(Task task, PlanningAspect planning, MovementAspect movement) {
        if(checkAction(task, planning)) movement.target = task.nextAction.actionTarget.getPosition();
        planning.actionChecked = false;
    }
    
    /**
     * Checks current action of task. Updates aspect flag of aspect and can fail task.
     * If sub action is created during check, it will be checked on next update.
     */
    private boolean checkAction(Task task, PlanningAspect planning) {
        ActionConditionStatusEnum result = task.nextAction.startCondition.get();
        planning.actionChecked = result == OK; // action is checked and did not generate sub actions
        if(result == FAIL) task.status = FAILED; // task will be removed
        return planning.actionChecked;
    }
}
