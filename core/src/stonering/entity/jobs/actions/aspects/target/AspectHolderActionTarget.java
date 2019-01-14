package stonering.entity.jobs.actions.aspects.target;

import stonering.entity.jobs.actions.Action;
import stonering.entity.local.AspectHolder;
import stonering.util.geometry.Position;

/**
 * Targets to some {@link AspectHolder}. Can be used with any single-tiled entities.
 */
public class AspectHolderActionTarget extends ActionTarget {
    private AspectHolder aspectHolder;

    public AspectHolderActionTarget(Action action, boolean exactTarget, boolean nearTarget, AspectHolder aspectHolder) {
        super(action, exactTarget, nearTarget);
        this.aspectHolder = aspectHolder;
    }

    @Override
    public Position getPosition() {
        return aspectHolder.getPosition();
    }
}
