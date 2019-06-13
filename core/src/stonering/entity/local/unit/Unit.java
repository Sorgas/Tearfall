package stonering.entity.local.unit;

import stonering.entity.local.Entity;
import stonering.util.geometry.Position;

/**
 * @author Alexander Kuzyakov on 06.10.2017.
 * <p>
 * Represents living creatures
 */
public class Unit extends Entity {

    public Unit(Position position) {
        super(position);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void turn() {
        aspects.values().forEach((aspect) -> aspect.turn());
    }
}