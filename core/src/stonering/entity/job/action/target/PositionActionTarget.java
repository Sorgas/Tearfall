package stonering.entity.job.action.target;

import stonering.util.geometry.Position;

/**
 * Targets to some tile.
 */
public class PositionActionTarget extends ActionTarget {
    private Position targetPosition;

    public PositionActionTarget(Position targetPosition, int targetPlacing) {
        super(targetPlacing);
        this.targetPosition = targetPosition;
    }

    @Override
    public Position getPosition() {
        return targetPosition;
    }
}
