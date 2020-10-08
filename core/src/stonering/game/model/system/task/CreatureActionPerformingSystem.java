package stonering.game.model.system.task;

import stonering.entity.job.Task;
import stonering.entity.job.action.ActionConditionStatusEnum;
import stonering.entity.job.action.MoveAction;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MovementAspect;
import stonering.entity.unit.aspects.job.TaskAspect;
import stonering.enums.action.ActionStatusEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.local_map.passage.PassageMap;
import stonering.game.model.system.EntitySystem;
import stonering.util.geometry.Position;
import stonering.util.geometry.PositionUtil;
import stonering.util.logging.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;
import static stonering.enums.action.TaskStatusEnum.*;

/**
 * System for performing tasks of units. Only tasks with status ACTIVE handled here.
 * Happy path:
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
    private PassageMap map;

    public CreatureActionPerformingSystem() {
        targetAspects.add(TaskAspect.class);
        targetAspects.add(MovementAspect.class);
    }

    @Override
    public void update(Unit unit) {
        MovementAspect movement = unit.get(MovementAspect.class);
        if (movement.target != null) return; // creature is moving
        TaskAspect taskAspect = unit.get(TaskAspect.class);
        Task task = taskAspect.task;
        // creature has active task but is not moving
        if (task != null && task.status == ACTIVE)
            checkTarget(taskAspect, movement, task);
    }

    /**
     * Checks if unit is in position for performing action, handles different cases of positioning.
     */
    private void checkTarget(TaskAspect taskAspect, MovementAspect movement, Task task) {
        if (!taskAspect.actionChecked && !(taskAspect.actionChecked = checkAction(task, taskAspect))) return; // action and task failed
        switch (task.nextAction.target.check(taskAspect.entity)) {
            case READY: // creature is in target, perform
                if (!handleReachingActionTarget(task, taskAspect)) return;
                break;
            case WAIT: // unit is far from reachable target
                movement.target = task.nextAction.target.getPosition(); // make unit move to target
                break;
            case STEP_OFF:
                handleStepOff(task, movement);
                break;
        }
        taskAspect.actionChecked = false; // reset to check again on target reach
    }

    /**
     * Performs action and removes it from task if it is completed.
     */
    private boolean handleReachingActionTarget(Task task, TaskAspect taskAspect) {
        task.nextAction.perform(); // perform checked action
        if (task.nextAction.status != ActionStatusEnum.COMPLETE) return false; // action not completed
        task.removeAction(task.nextAction); // remove non initial complete action
        if (task.isFinished()) task.status = COMPLETE; // all actions completed
        return true;
    }

    /**
     * Finds position to free target tile and sets it as movement target, or fails task.
     */
    private void handleStepOff(Task task, MovementAspect movement) {
        PositionUtil.allNeighbour.stream()
                .map(delta -> Position.add(task.performer.position, delta))
                .filter(position -> map().hasPathBetweenNeighbours(position, task.performer.position))
                .findAny() // any position to step off
                .map(MoveAction::new)
                .ifPresentOrElse(task::addFirstPreAction, () -> task.status = FAILED);
    }

    /**
     * Checks current action of task. Updates aspect flag of aspect and can fail task.
     * If sub action is created during check, it will be checked on next update.
     */
    private boolean checkAction(Task task, TaskAspect taskAspect) {
        Logger.TASKS.logDebug("checking action");
        ActionConditionStatusEnum result = task.nextAction.startCondition.get();
        if (result == FAIL) task.status = FAILED; // task will be removed
        return taskAspect.actionChecked = (result == OK); // action is checked and did not generate sub actions
    }

    private PassageMap map() {
        return map == null ? map = GameMvc.model().get(LocalMap.class).passageMap : map;
    }
}
