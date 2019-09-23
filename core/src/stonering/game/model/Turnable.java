package stonering.game.model;

import stonering.enums.time.TimeUnitEnum;

/**
 * Object that can make turns.
 * Update logic in {@mecthod turn} and {@method turnInterval} should not be the same.
 *
 * @author Alexander on 07.10.2018.
 */
public abstract class Turnable {

    /**
     * Updates this object.
     */
    public void turn() {
    }

    public void turn(TimeUnitEnum unit) {

    }
}
