package stonering.entity;

import stonering.util.geometry.Position;
import stonering.util.global.Logger;

/**
 * Stores {@link Entity}'s position;
 * @author Alexander on 03.09.2019.
 */
public class PositionAspect extends Aspect {
    public Position position;

    public PositionAspect(Entity entity, Position position) {
        super(entity);
        if(entity == null) Logger.GENERAL.logError("Creation of aspect with null entity.");
        this.position = position;
    }
}
