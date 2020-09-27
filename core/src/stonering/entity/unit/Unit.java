package stonering.entity.unit;

import stonering.entity.VectorPositionEntity;
import stonering.enums.unit.race.CreatureType;
import stonering.util.geometry.Position;

/**
 * Object for living creatures.
 *
 * @author Alexander Kuzyakov on 06.10.2017.
 */
public class Unit extends VectorPositionEntity {
    public final CreatureType type;

    public Unit(Position position, CreatureType type) {
        super(position);
        this.type = type;
    }

    public CreatureType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.title;
    }
}