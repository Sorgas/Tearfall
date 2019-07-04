package stonering.entity.unit;

import stonering.entity.PositionedEntity;
import stonering.util.geometry.Position;

/**
 * @author Alexander Kuzyakov on 06.10.2017.
 * <p>
 * Represents living creatures
 */
public class Unit extends PositionedEntity {

    public Unit(Position position) {
        super(position);
    }

    public void turn() {
        aspects.values().forEach((aspect) -> aspect.turn());
    }
}