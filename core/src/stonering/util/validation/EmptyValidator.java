package stonering.util.validation;

import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 05.05.2019.
 */
public class EmptyValidator extends PositionValidator {
    @Override
    public boolean validate(LocalMap map, Position position) {
        return true;
    }
}
