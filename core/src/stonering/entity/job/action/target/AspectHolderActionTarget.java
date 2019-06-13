package stonering.entity.job.action.target;

import stonering.entity.local.Entity;
import stonering.entity.local.PositionAspect;
import stonering.util.geometry.Position;

/**
 * Targets to some {@link Entity}. Can be used with any single-tiled entities.
 */
public class AspectHolderActionTarget extends ActionTarget {
    private Entity entity;

    public AspectHolderActionTarget(Entity entity, boolean exactTarget, boolean nearTarget) {
        super(exactTarget, nearTarget);
        this.entity = entity;
    }

    @Override
    public Position getPosition() {
        return entity.getAspect(PositionAspect.class).position;
    }
}
