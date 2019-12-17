package stonering.entity.job.action;

import stonering.util.global.Executor;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Phase of {@link PhasedAction}.
 * Accepts work amount passed from action to make progress.
 * Can perform some logic on start, finish and during process.
 * Has goal which can end phase, even before all required work amount is passed.
 *
 * @author Alexander on 15.12.2019.
 */
public abstract class ActionPhase {
    public final float requiredAmount; // max amount of work
    public float progress; // current amount of added work
    public Executor onStart; // performed on phase start
    public Executor onFinish; // performed on phase finish
    public Consumer<Float> progressConsumer; // performs logic when phase is in progress
    public Supplier<Boolean> finishGoal; // when reached, phase ends

    protected ActionPhase(float requiredAmount) {
        this.requiredAmount = requiredAmount;
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
        if(finishGoal.get() || progress >= requiredAmount) {
            onFinish.execute();
            return true;
        }
        return false;
    }
}
