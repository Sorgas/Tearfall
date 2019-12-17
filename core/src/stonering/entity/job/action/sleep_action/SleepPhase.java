package stonering.entity.job.action.sleep_action;

import stonering.entity.job.action.ActionPhase;

/**
 * Creature sleeps during this phase.
 *
 * @author Alexander on 17.12.2019.
 */
public class SleepPhase extends ActionPhase {

    protected SleepPhase(float requiredAmount) {
        super(requiredAmount);
        onStart = () -> {
            // disable vision
            // decrease hearing
        };
        onFinish = () -> {
            // restore vision and hearing
        };
        progressConsumer = (delta) -> {
            // decrease fatigue by delta
        };
        finishGoal = () -> {
            // fatigue is zero
            return false;
        };
    }
}
