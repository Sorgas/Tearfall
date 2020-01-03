package stonering.game.model.system.task;

import stonering.entity.job.Task;
import stonering.entity.job.action.target.ActionTargetStatusEnum;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.enums.action.ActionStatusEnum;
import stonering.game.model.system.EntitySystem;

import static stonering.enums.action.TaskStatusEnum.*;

/**
 * System for performing tasks of units. Calls perform() of task's actions, and updates task statuses.
 * Only tasks with status ACTIVE handled here. This system handles only active tasks.
 * //TODO handle cases when creature cannon move. all actions with distant target are failed.
 *
 * @author Alexander on 29.10.2019.
 */
public class CreatureTaskPerformingSystem extends EntitySystem<Unit> {

    public CreatureTaskPerformingSystem() {
        targetAspects.add(PlanningAspect.class);
        targetAspects.add(MovementAspect.class);
    }

    /**
     * Updates state of given unit's active task. Selects new task, fails and finishes current.
     * Tasks logic is invoked from here.
     */
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
        ActionTargetStatusEnum checkResult = task.nextAction.actionTarget.check(planning.getEntity().position);
        switch (checkResult) {
            case READY:
                handleReachingActionTarget(task, planning);
                break;
            case WAIT: // set target for moving
                movement.target = task.nextAction.actionTarget.getPosition();
                break;
            case FAIL: // target unreachable
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
    private void handleReachingActionTarget(Task task, PlanningAspect aspect) {
        if (!aspect.actionChecked) {
            checkAction(task, aspect); // check before performing
            return;
        }
        task.nextAction.perform();
        if(task.nextAction.status == ActionStatusEnum.COMPLETE) {
            if (task.isNoActionsLeft()) { // was last action in task
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
        switch (task.nextAction.startCondition.get()) {
            case OK:
                aspect.actionChecked = true;
                return;
            case NEW:
                aspect.actionChecked = false; // new action is not yet checked
                return;
            case FAIL:
                task.status = FAILED;
                aspect.actionChecked = false; // task will be removed
        }
    }
}
