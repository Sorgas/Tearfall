package stonering.entity.job.action;

import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.Task;
import stonering.enums.action.ActionStatusEnum;
import stonering.util.global.Executor;

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
    protected String usedSkill;
    public final ActionTarget actionTarget;
    public Supplier<ActionConditionStatusEnum> startCondition;
    public Executor onStart; // performed on phase start
    public Consumer<Float> progressConsumer; // performs logic
    public Supplier<Boolean> finishCondition; // when reached, action ends
    public Executor onFinish; // performed on phase finish
    public ActionStatusEnum status;

    //TODO remove
    public float baseSpeed = 0.01f; // distracted from workAmount to make action progress.

    protected Action(ActionTarget actionTarget) {
        this.actionTarget = actionTarget;
        actionTarget.setAction(this);
        startCondition = () -> OK;
        onStart = () -> {};
        progressConsumer = (delta) -> {};
        finishCondition = () -> true;
        onFinish = () -> {};
    }

    /**
     * Performs action logic. Changes status.
     */
    public final void perform() {
        if(status == OPEN) { // first execution of perform()
            status = ACTIVE;
            onStart.execute();
        }
        progressConsumer.accept(0.01f); //
        if(finishCondition.get()) { // last execution of perform()
            status = COMPLETE;
            onFinish.execute();
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

    public float getProgress() {
        return 0;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
