package stonering.entity.local;

import stonering.util.geometry.Position;

/**
 * Aspect for entities that have position in {@link stonering.game.model.local_map.LocalMap}
 *
 * @author Alexander_Kuzyakov on 13.06.2019.
 */
public class PositionAspect extends Aspect {
    public Position position;

    public PositionAspect(Entity entity) {
        super(entity);
    }

    public PositionAspect(Entity entity, Position position) {
        super(entity);
        this.position = position;
    }
}
