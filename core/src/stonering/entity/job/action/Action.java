package stonering.entity.job.action;

import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.Task;
import stonering.enums.action.ActionStatusEnum;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static stonering.entity.job.action.ActionConditionStatusEnum.OK;
import static stonering.enums.action.ActionStatusEnum.*;

/**
 * Action of a unit. All units behaviour except moving are defined in actions. Actions are parts of {@link Task}.
 * During performing unit adds certain amount of 'work' to an action. Skills, health and other conditions may influence unit's work speed.
 * <p>
 * Action consist of several parts:
 * Target where unit should be to perform action.
 * Start condition - to be met before performing is started, can create additional actions.
 * Start function - executed once.
 * Speed updater - calculates performing speed when performing starts.
 * Progress consumer function - executed many times. Does additive changes to model during action performing.
 * Finish condition - action finishes, when condition is met.
 * Finish function - executed once.
 * <p>
 * Additional actions are created and added to task, when start condition is not met, but could be after additional action(equip tool, bring items).
 * If start condition is not met, action and its task are failed.
 * Default implementation is an action with no requirements nor effect, which is finished immediately;
 */
public abstract class Action {
    public Task task; // can be modified during execution
    public final ActionTarget actionTarget;
    public ActionStatusEnum status;

    public Supplier<ActionConditionStatusEnum> startCondition;
    public Runnable onStart; // performed on phase start
    public Supplier<Float> speedUpdater; // calculates speed of performing
    public Consumer<Float> progressConsumer; // performs logic
    public Supplier<Boolean> finishCondition; // when reached, action ends
    public Runnable onFinish; // performed on phase finish

    public float speed = -1;
    public float progress = 0;
    public float maxProgress = 1;

    protected Action(ActionTarget actionTarget) {
        this.actionTarget = actionTarget;
        actionTarget.setAction(this);
        startCondition = () -> OK;
        onStart = () -> {};
        progressConsumer = (delta) -> progress += delta;
        speedUpdater = () -> 1f;
        finishCondition = () -> progress >= maxProgress;
        onFinish = () -> {};
    }

    /**
     * Performs action logic. Changes status.
     */
    public final void perform() {
        if(status == OPEN) { // first execution of perform()
            status = ACTIVE;
            onStart.run();
            speed = speedUpdater.get();
        }
        progressConsumer.accept(speedUpdater.get());
        if(finishCondition.get()) { // last execution of perform()
            status = COMPLETE;
            onFinish.run();
            task.finishAction(this);
        }
    }

    /**
     * Resets task state as it had not been started.
     */
    public void reset() {
        startCondition = () -> OK;
        onStart = () -> {};
        progressConsumer = (amount) -> {};
        finishCondition = () -> true;
        onFinish = () -> {};
        status = OPEN;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
