package stonering.entity.local.building.validators;

import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Used to validate position with some logic.
 *
 * @author Alexander on 23.11.2018.
 */
public abstract class PositionValidator {

    public abstract boolean validate(LocalMap map, Position position);
}
