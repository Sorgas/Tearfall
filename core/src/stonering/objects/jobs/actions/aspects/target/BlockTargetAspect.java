package stonering.objects.jobs.actions.aspects.target;

import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;

public class BlockTargetAspect extends TargetAspect{
    private Position targetPosition;

    public BlockTargetAspect(Action action, Position targetPosition, boolean exactTarget) {
        super(action);
        this.targetPosition = targetPosition;
        this.exactTarget = exactTarget;
    }

    @Override
    public Position getTargetPosition() {
        return targetPosition;
    }
}
