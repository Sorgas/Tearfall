package stonering.objects.jobs.actions.aspects.target;

import stonering.global.utils.Position;

public abstract class TargetAspect {
    protected Position targetPosition;
    protected boolean exactTarget;

    public Position getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Position targetPosition) {
        this.targetPosition = targetPosition;
    }

    public boolean isExactTarget() {
        return exactTarget;
    }

    public void setExactTarget(boolean exactTarget) {
        this.exactTarget = exactTarget;
    }
}
