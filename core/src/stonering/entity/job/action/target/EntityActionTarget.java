package stonering.entity.job.action.target;

import stonering.entity.Entity;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.util.geometry.Position;

/**
 * Targets to some {@link Entity}. Can be used with any single-tiled entities.
 * // TODO make generic
 */
public class EntityActionTarget extends ActionTarget {
    public Entity entity;

    public EntityActionTarget(Entity entity, ActionTargetTypeEnum placement) {
        super(placement);
        this.entity = entity;
    }

    @Override
    public Position getPosition() {
        return entity.position;
    }
}
