package stonering.entity.job.action.phase;

import stonering.util.global.Executor;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Phase of {@link PhasedAction}.
 * Accepts work passed from action to make progress.
 * Can perform some logic on start, finish and during process.
 * Phase is ended, when the finish goal is reached.1
 *
 * @author Alexander on 15.12.2019.
 */
public abstract class ActionPhase {
    protected final PhasedAction action;
    public float progress; // total added work
    public Executor onStart; // performed on phase start
    public Executor onFinish; // performed on phase finish
    public Consumer<Float> progressConsumer; // performs logic when phase is in progress
    public Supplier<Boolean> finishGoal; // when reached, phase ends

    protected ActionPhase(PhasedAction action) {
        this.action = action;
        progress = 0;
        onStart = () -> {};
        onFinish = () -> {};
        progressConsumer = (amount) -> {};
        finishGoal = () -> false;
    }

    public boolean perform(float delta) {
        if(progress == 0) onStart.execute();
        progressConsumer.accept(delta);
        progress += delta;
        if(isFinished()) {
            onFinish.execute();
            return true;
        }
        return false;
    }

    protected boolean isFinished() {
        return finishGoal.get();
    }
}
