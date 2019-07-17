package stonering.game.model;

import stonering.enums.time.TimeUnitEnum;

/**
 * Components of this class can be updated once in some interval of time.
 *
 * @author Alexander Kuzyakov
 * @see stonering.entity.environment.GameCalendar
 */
public abstract class IntervalTurnable extends Turnable {

    public void turnInterval(TimeUnitEnum unit) {

    }
}
