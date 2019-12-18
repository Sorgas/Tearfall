package stonering.entity.job.action.sleep_action;

import stonering.entity.job.action.phase.PhasedAction;
import stonering.entity.job.action.phase.TimedActionPhase;

/**
 * Create is awake and tries to sleep during this phase.
 * End condition is not set, so this phase can end only when the timer expires
 * meaning it's hard to fall asleep for creature due to negative effects.
 *
 * @author Alexander on 17.12.2019.
 */
public class RestPhase extends TimedActionPhase {

    public RestPhase(PhasedAction action, int numberOfTicks) {
        super(action, numberOfTicks);
        progressConsumer = (delta) -> { // creature rests even while not sleeping
            // decrease fatigue slightly
        };
    }
}
