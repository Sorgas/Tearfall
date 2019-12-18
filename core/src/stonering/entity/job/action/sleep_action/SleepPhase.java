package stonering.entity.job.action.sleep_action;

import stonering.entity.job.action.ActionPhase;
import stonering.entity.job.action.PhasedAction;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.health.HealthParameterState;
import stonering.enums.unit.health.HealthParameterEnum;

/**
 * Creature sleeps during this phase.
 * TODO
 *
 * @author Alexander on 17.12.2019.
 */
public class SleepPhase extends ActionPhase {
    private final HealthParameterState targetParameter;

    protected SleepPhase(float requiredAmount, PhasedAction action) {
        super(action);
        targetParameter = action.task.performer.getAspect(HealthAspect.class).parameters.get(HealthParameterEnum.FATIGUE);
        onStart = () -> {
            // disable vision
            // decrease hearing
        };
        onFinish = () -> {
            // restore vision and hearing
        };
        progressConsumer = (delta) -> targetParameter.current -= delta; // decrease fatigue
        finishGoal = () -> targetParameter.current <= 0; // stop sleeping
    }
}
