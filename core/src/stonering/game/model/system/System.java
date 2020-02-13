package stonering.game.model.system;

import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.GameModel;

/**
 * Part of {@link GameModel} for updating model.
 * Specifies interval of updates.
 *
 * @author Alexander on 13.02.2020
 */
public abstract class System {
    public TimeUnitEnum updateInterval = TimeUnitEnum.TICK;
}
