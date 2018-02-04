package stonering.objects.jobs.actions.aspects.target;

import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;

public class BlockTargetAspect extends TargetAspect{

    public BlockTargetAspect(Action action, Position targetPosition) {
        super(action, targetPosition);
        exactTarget = false;
    }
}
