package stonering.entity;

import stonering.util.geometry.Position;

/**
 * @author Alexander on 03.09.2019.
 */
public class PositionedEntity extends Entity {
    public Position position;

    public PositionedEntity(Position position) {
        this.position = position;
    }
}
