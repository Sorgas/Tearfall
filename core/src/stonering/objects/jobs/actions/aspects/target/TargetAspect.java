package stonering.objects.jobs.actions.aspects.target;

import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;

public abstract class TargetAspect {
    protected Action action;
    protected boolean exactTarget;

    public TargetAspect(Action action) {
        this.action = action;
    }

    public abstract Position getTargetPosition();

    public boolean isExactTarget() {
        return exactTarget;
    }
}
