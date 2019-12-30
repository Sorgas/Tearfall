package stonering.entity.job.action;

import stonering.entity.job.action.target.PositionActionTarget;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.util.geometry.Position;

import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

/**
 * Action for moving to tile. Has no check or logic.
 */
public class MoveAction extends Action {

    public MoveAction(Position to) {
        super(new PositionActionTarget(to, ActionTargetTypeEnum.EXACT));
    }

    @Override
    public ActionConditionStatusEnum check() {
        return OK;
    }

    @Override
    public void performLogic() {
    }

    @Override
    public String toString() {
        return "Move name";
    }
}
