package stonering.util.lang;

import stonering.enums.time.TimeUnitEnum;

/**
 * Object that can be updated over some time interval {@link TimeUnitEnum}.
 *
 * @author Alexander on 07.10.2018.
 */
public interface Updatable {

    void update(TimeUnitEnum unit);
}
