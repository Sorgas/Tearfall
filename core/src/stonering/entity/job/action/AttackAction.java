package stonering.entity.job.action;

import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

import stonering.entity.Entity;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.enums.action.ActionTargetTypeEnum;

/**
 *
 *
 * @author Alexander on 8/4/2020
 */
public class AttackAction extends Action {

    protected AttackAction(Entity target) {
        super(new EntityActionTarget(target, ActionTargetTypeEnum.NEAR));

        startCondition = () -> OK;
    }
}
