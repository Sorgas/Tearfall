package stonering.entity.local.unit;

import stonering.util.geometry.Position;
import stonering.entity.local.AspectHolder;

/**
 * @author Alexander Kuzyakov on 06.10.2017.
 * <p>
 * Represents living creatures
 */
public class Unit extends AspectHolder {

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