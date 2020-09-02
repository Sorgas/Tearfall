package stonering.entity.unit;

import stonering.entity.VectorPositionEntity;
import stonering.enums.unit.race.CreatureType;
import stonering.util.geometry.Position;

/**
 * @author Alexander Kuzyakov on 06.10.2017.
 * <p>
 * Represents living creatures
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