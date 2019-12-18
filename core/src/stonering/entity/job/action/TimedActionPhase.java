package stonering.entity.job.action;

/**
 * This {@link ActionPhase} is limited by number of ticks.
 * When end condition is met or tick counter is over, phase ends.
 *
 * @author Alexander on 18.12.2019.
 */
public class TimedActionPhase extends ActionPhase {
    protected TimedActionPhase(PhasedAction action) {
        super(action);

    }
}
