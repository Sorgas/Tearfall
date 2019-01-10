package stonering.entity.local.building.validators;

import stonering.game.core.model.LocalMap;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 23.11.2018.
 */
public abstract class PositionValidator {

    public abstract boolean validate(LocalMap map, Position position);
}
