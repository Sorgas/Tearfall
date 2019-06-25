package stonering.entity.local;

import stonering.util.geometry.Position;

/**
 * Simple entity with position.
 * Adds {@link PositionAspect} to itself.
 *
 * @author Alexander_Kuzyakov on 13.06.2019.
 */
public class PositionedEntity extends Entity {

    public PositionedEntity() {
        addAspect(new PositionAspect(this));
    }

    public PositionedEntity(Position position) {
        addAspect(new PositionAspect(this, position));
    }

    public Position getPosition() {
        return getAspect(PositionAspect.class).position;
    }

    public void setPosition(Position position) {
        getAspect(PositionAspect.class).position = position;
    }
}
