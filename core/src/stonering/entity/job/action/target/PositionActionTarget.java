package stonering.entity.job.action.target;

import stonering.enums.action.ActionTargetTypeEnum;
import stonering.util.geometry.Position;

/**
 * Targets to some tile.
 */
public class PositionActionTarget extends ActionTarget {
    private Position targetPosition;

    public PositionActionTarget(Position targetPosition, ActionTargetTypeEnum placement) {
        super(placement);
        this.targetPosition = targetPosition;
    }

    @Override
    public Position getPosition() {
        return targetPosition;
    }
}
