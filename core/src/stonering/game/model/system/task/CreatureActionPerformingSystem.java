package stonering.game.model.system.task;

import stonering.entity.job.Task;
import stonering.entity.job.action.ActionConditionStatusEnum;
import stonering.entity.job.action.target.ActionTargetStatusEnum;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.enums.action.ActionStatusEnum;
import stonering.game.model.system.EntitySystem;
import stonering.util.global.Logger;

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
        PlanningAspect planning = unit.get(PlanningAspect.class);
        MovementAspect movement = unit.get(MovementAspect.class);
        Task task = planning.task;
        // creature has active task but is not moving
        if (task != null && task.status == ACTIVE && movement.target == null)
            checkTarget(planning, movement, task);
    }

    /**
     * Checks if unit is in position for performing action, handles different cases of positioning.
     * Creating new action during target check, will be handled on next update.
     */
    private void checkTarget(PlanningAspect planning, MovementAspect movement, Task task) {
        Logger.TASKS.logDebugn("Checking target of " + task.nextAction + ": ");
        ActionTargetStatusEnum check = task.nextAction.target.check(planning.entity);
        System.out.println(" " + check.toString());
        switch (check) {
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
        if(aspect.actionChecked || checkAction(task, aspect)) {
            System.out.println("perform");
            task.nextAction.perform(); // perform checked action
            if(task.nextAction.status == ActionStatusEnum.COMPLETE) { // action completed
                task.removeAction(task.nextAction); // remove non initial complete action
                aspect.actionChecked = false; // checked action has been removed
                if(task.isNoActionsLeft()) task.status = COMPLETE; // all actions completed
            }
        }
    }

    private void handleStartMovement(Task task, PlanningAspect planning, MovementAspect movement) {
        if (checkAction(task, planning))
            movement.target = task.nextAction.target.getPosition(); // enable moving to target of successfully checked action
        planning.actionChecked = false; // reset to check again on target reach
    }

    /**
     * Checks current action of task. Updates aspect flag of aspect and can fail task.
     * If sub action is created during check, it will be checked on next update.
     */
    private boolean checkAction(Task task, PlanningAspect planning) {
        ActionConditionStatusEnum result = task.nextAction.startCondition.get();
        if (result == FAIL) task.status = FAILED; // task will be removed
        return planning.actionChecked = (result == OK); // action is checked and did not generate sub actions
    }
}
