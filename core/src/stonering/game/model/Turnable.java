package stonering.game.model;

import stonering.enums.time.TimeUnitEnum;

/**
 * Object that can make turns.
 * Update logic in {@method turnUnit} and {@method turnInterval} should not be the same.
 *
 * @author Alexander on 07.10.2018.
 */
public interface Turnable {

    /**
     * Updates this object.
     */
    default void turn() {
    }

    default void turnUnit(TimeUnitEnum unit) {
    }
}
