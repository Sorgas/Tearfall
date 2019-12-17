package stonering.entity.job.action.sleep_action;

import stonering.entity.job.action.ActionPhase;

/**
 * Create is awake and tries to sleep during this phase.
 *
 * @author Alexander on 17.12.2019.
 */
public class RestPhase extends ActionPhase {

    protected RestPhase(float requiredAmount) {
        super(requiredAmount);
        progressConsumer = (delta) -> { // creature rests even while not sleeping
            // decrease fatigue slightly
        };
    }
}
