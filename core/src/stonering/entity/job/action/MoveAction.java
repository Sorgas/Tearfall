package stonering.entity.job.action;

import stonering.entity.job.action.target.PositionActionTarget;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.util.geometry.Position;

/**
 * Action for moving to tile. Has no check or logic.
 */
public class MoveAction extends Action {

    public MoveAction(Position to) {
        super(new PositionActionTarget(to, ActionTargetTypeEnum.EXACT));
    }

    @Override
    public String toString() {
        return "Move name";
    }
}
