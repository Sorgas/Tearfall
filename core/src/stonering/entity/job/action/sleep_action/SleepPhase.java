package stonering.entity.job.action.sleep_action;

import stonering.entity.job.action.phase.PhasedAction;
import stonering.entity.job.action.phase.TimedActionPhase;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.health.HealthParameterState;
import stonering.enums.unit.health.HealthParameterEnum;
import stonering.util.global.Logger;

/**
 * Creature sleeps during this phase.
 * Phase has maximum length, creature awakes when it is expired.
 * Creature can also awake when fatigue is restored.
 * TODO
 *
 * @author Alexander on 17.12.2019.
 */
public class SleepPhase extends TimedActionPhase {
    private HealthParameterState targetParameter;

    protected SleepPhase(PhasedAction action, int numberOfTicks) {
        super(action, numberOfTicks);

        onStart = () -> {
            targetParameter = action.task.performer.getAspect(HealthAspect.class).parameters.get(HealthParameterEnum.FATIGUE);
            Logger.TASKS.logDebug("start sleep phase");
            // disable vision
            // decrease hearing
        };
        onFinish = () -> {
            Logger.TASKS.logDebug("finish sleep phase");
            // restore vision and hearing
            // if fatigue is not restored completely, apply negative mood buff
        };
        progressConsumer = (delta) -> {
            targetParameter.current -= delta; // decrease fatigue
            Logger.TASKS.logDebug("restoring fatigue for " + delta + " new value " + targetParameter.current);
        };
        finishGoal = () -> targetParameter.current <= 0; // stop sleeping
    }
}
