package stonering.entity.job.action;

import stonering.game.model.system.task.CreatureActionPerformingSystem;

/**
 * Enumeration of {@link Action} check results.
 * Actions are checked by {@link CreatureActionPerformingSystem}.
 *
 * @author Alexander on 30.12.2019.
 */
public enum ActionConditionStatusEnum {
    OK, // if checked successfully.
    NEW, // if new sub action created and added to task.
    FAIL // if unable perform action or create sub action.
}
