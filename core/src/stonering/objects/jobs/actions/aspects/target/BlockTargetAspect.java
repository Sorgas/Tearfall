package stonering.objects.jobs.actions.aspects.target;

import stonering.global.utils.Position;

public class BlockTargetAspect extends TargetAspect{
    private Position target;

    public BlockTargetAspect(Position target) {
        this.target = target;
    }

    public Position getTarget() {
        return target;
    }

    public void setTarget(Position target) {
        this.target = target;
    }
}
