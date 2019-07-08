package stonering.entity.unit;

import stonering.entity.PositionedEntity;
import stonering.enums.unit.CreatureType;

/**
 * @author Alexander Kuzyakov on 06.10.2017.
 * <p>
 * Represents living creatures
 */
public class Unit extends PositionedEntity {
    CreatureType type;

    public Unit(CreatureType type) {
        this.type = type;
    }

    public CreatureType getType() {
        return type;
    }
}