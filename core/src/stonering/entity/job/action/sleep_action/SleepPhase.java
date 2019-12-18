package stonering.entity.job.action.sleep_action;

import stonering.entity.job.action.phase.PhasedAction;
import stonering.entity.job.action.phase.TimedActionPhase;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.health.HealthParameterState;
import stonering.enums.unit.health.HealthParameterEnum;

/**
 * Creature sleeps during this phase.
 * Phase has maximum length, creature awakes when it is expired.
 * Creature can also awake when fatigue is restored.
 * TODO
 *
 * @author Alexander on 17.12.2019.
 */
public class SleepPhase extends TimedActionPhase {
    private final HealthParameterState targetParameter;

    protected SleepPhase(PhasedAction action, int numberOfTicks) {
        super(action, numberOfTicks);
        targetParameter = action.task.performer.getAspect(HealthAspect.class).parameters.get(HealthParameterEnum.FATIGUE);
        onStart = () -> {
            // disable vision
            // decrease hearing
        };
        onFinish = () -> {
            // restore vision and hearing
            // if fatigue is not restored completely, apply negative mood buff
        };
        progressConsumer = (delta) -> targetParameter.current -= delta; // decrease fatigue
        finishGoal = () -> targetParameter.current <= 0; // stop sleeping
    }
}
