package stonering.game.model;

/**
 * @author Alexander Kuzyakov
 */
public abstract class IntervalTurnable extends Turnable {
    /**
     * To be called once in a minute.
     */
    public void turnMinute() {
    }

    /**
     * To be called once in a hour.
     */
    public void turnHour() {
    }

    /**
     * To be called once in a day.
     */
    public void turnDay() {
    }

    /**
     * To be called once in a month.
     */
    public void turnMonth() {
    }

    /**
     * To be called once in a year.
     */
    public void turnYear() {
    }
}
