package stonering.objects.jobs.actions.aspects.target;

import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;

public abstract class TargetAspect {
    protected Action action;
    protected Position targetPosition;
    protected boolean exactTarget;

    public TargetAspect(Action action, Position targetPosition) {
        this.action = action;
        this.targetPosition = targetPosition;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    public boolean isExactTarget() {
        return exactTarget;
    }
}
