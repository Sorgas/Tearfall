package stonering.entity.job.action.target;

/**
 * This values are used when checking action targets (whether creature is in position for performing action).
 *
 * @author Alexander_Kuzyakov on 23.10.2019.
 */
public enum ActionTargetStatusEnum {
    READY, // target position reached
    WAIT, // target position no reached
    NEW, // new action created. planning system should update task
    FAIL // failed to create action
}
