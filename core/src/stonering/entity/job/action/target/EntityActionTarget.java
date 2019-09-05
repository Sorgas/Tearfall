package stonering.entity.job.action.target;

import stonering.entity.Entity;
import stonering.util.geometry.Position;

/**
 * Targets to some {@link Entity}. Can be used with any single-tiled entities.
 */
public class EntityActionTarget extends ActionTarget {
    protected Entity entity;

    public EntityActionTarget(Entity entity, int targetPlacement) {
        super(targetPlacement);
        this.entity = entity;
    }

    @Override
    public Position getPosition() {
        return entity.position;
    }
}
