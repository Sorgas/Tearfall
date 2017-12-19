package stonering.objects.jobs.actions.aspects.target;

import stonering.global.utils.Position;

public abstract class TargetAspect {
    private Position targetPosition;

    public Position getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Position targetPosition) {
        this.targetPosition = targetPosition;
    }
}
