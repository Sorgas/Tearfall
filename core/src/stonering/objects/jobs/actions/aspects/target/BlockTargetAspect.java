package stonering.objects.jobs.actions.aspects.target;

import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;

public class BlockTargetAspect extends TargetAspect{
    private Position targetPosition;

    public BlockTargetAspect(Action action, Position targetPosition, boolean exactTarget, boolean nearTarget) {
        super(action, exactTarget, nearTarget);
        this.targetPosition = targetPosition;
    }

    @Override
    public Position getTargetPosition() {
        return targetPosition;
    }
}
