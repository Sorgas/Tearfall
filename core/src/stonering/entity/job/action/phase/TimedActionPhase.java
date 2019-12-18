package stonering.entity.job.action.phase;

/**
 * This {@link ActionPhase} is limited by number of ticks.
 * It ends when timer expires or end condition is met.
 *
 * @author Alexander on 18.12.2019.
 */
public abstract class TimedActionPhase extends ActionPhase {
    private int numberOfTicks;
    private int tickCounter;

    protected TimedActionPhase(PhasedAction action, int numberOfTicks) {
        super(action);
        this.numberOfTicks = numberOfTicks;
        tickCounter = 0;
    }

    @Override
    protected boolean isFinished() {
        return tickCounter++ >= numberOfTicks || super.isFinished();
    }
}
