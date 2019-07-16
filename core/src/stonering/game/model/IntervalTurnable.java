package stonering.game.model;

/**
 * Components of this class can be updated once in some interval of time.
 *
 * @author Alexander Kuzyakov
 * @see stonering.entity.environment.GameCalendar
 */
public abstract class IntervalTurnable extends Turnable {
    /**
     * To be called once in a specified interval.
     */
    public void turnInterval() {
    }
}
