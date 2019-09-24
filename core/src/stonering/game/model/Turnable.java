package stonering.game.model;

import stonering.enums.time.TimeUnitEnum;

/**
 * Object that can make turns.
 * Update logic in {@method turn} and {@method turnInterval} should not be the same.
 *
 * @author Alexander on 07.10.2018.
 */
public interface Turnable {

    /**
     * Updates this object.
     */
    void turn();

    void turn(TimeUnitEnum unit);
}
