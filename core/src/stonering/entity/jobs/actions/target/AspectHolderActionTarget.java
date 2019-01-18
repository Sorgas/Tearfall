package stonering.entity.jobs.actions.target;

import stonering.entity.local.AspectHolder;
import stonering.util.geometry.Position;

/**
 * Targets to some {@link AspectHolder}. Can be used with any single-tiled entities.
 */
public class AspectHolderActionTarget extends ActionTarget {
    private AspectHolder aspectHolder;

    public AspectHolderActionTarget(AspectHolder aspectHolder, boolean exactTarget, boolean nearTarget) {
        super(exactTarget, nearTarget);
        this.aspectHolder = aspectHolder;
    }

    @Override
    public Position getPosition() {
        return aspectHolder.getPosition();
    }
}
