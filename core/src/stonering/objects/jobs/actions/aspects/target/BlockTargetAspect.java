package stonering.objects.jobs.actions.aspects.target;

import stonering.global.utils.Position;

public class BlockTargetAspect extends TargetAspect{
    public BlockTargetAspect(Position target) {
        targetPosition = target;
        exactTarget = false;
    }
}
