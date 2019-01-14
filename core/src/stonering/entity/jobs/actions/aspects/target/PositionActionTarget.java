package stonering.entity.jobs.actions.aspects.target;

import stonering.entity.jobs.actions.Action;
import stonering.util.geometry.Position;

/**
 * Targets to some tile.
 */
public class PositionActionTarget extends ActionTarget {
    private Position targetPosition;

    public PositionActionTarget(Action action, Position targetPosition, boolean exactTarget, boolean nearTarget) {
        super(action, exactTarget, nearTarget);
        this.targetPosition = targetPosition;
    }

    @Override
    public Position getPosition() {
        return targetPosition;
    }
}
