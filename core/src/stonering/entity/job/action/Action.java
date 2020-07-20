package stonering.entity.job.action;

import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.Task;
import stonering.enums.action.ActionStatusEnum;
import stonering.game.model.system.unit.CreaturePlanningSystem;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.NEW;
import static stonering.enums.action.ActionStatusEnum.*;

/**
 * Action of a unit. All units behaviour except moving are defined in actions. Actions are parts of {@link Task}.
 * During performing unit adds certain amount of 'work' to an action. Skills, health and other conditions may influence unit's work speed.
 * <p>
 * Action consist of several parts:
 * <p>
 * 1. Target where unit should be to perform action. Before taking action from container, target reachability is checked.
 * 2. Taking condition - to be met before task is taken from container. 
 * Start condition - to be met before performing is started, can create additional actions e.g. bringing materials to workbench.
 * OnStart function - executed once.
 * Progress consumer function - executed many times. Does additive changes to model during action performing.
 * Finish condition - action finishes, when condition is met.
 * OnFinish function - executed once.
 * <p>
 * Additional actions are created and added to task, when start condition is not met, but could be after additional action(equip tool, bring items).
 * If start condition is not met, action and its task are failed.
 * Default implementation is an action with no requirements nor effect, which is finished immediately;
 */
public abstract class Action {
    public Task task; // can be modified during execution
    public final ActionTarget target;
    public ActionStatusEnum status = OPEN;

    /**
     * Condition to be met before task with this action is assigned to unit.
     * Should check tool, consumed items, target reachability for performer. 
     * Can use {@code task.performer}, as it is assigned to task before calling by {@link CreaturePlanningSystem}.
     */
    public Supplier<Boolean> takingCondition;
    public Supplier<ActionConditionStatusEnum> startCondition; // called before performing, can create sub actions
    public Runnable onStart; // performed on phase start
    public Consumer<Float> progressConsumer; // performs logic
    public Supplier<Boolean> finishCondition; // when reached, action ends
    public Runnable onFinish; // performed on phase finish

    public float progress;
    
    // should be set before performing
    public float speed = 1;
    public float maxProgress = 1;

    protected Action(ActionTarget target) {
        this.target = target;
        target.setAction(this);
        takingCondition = () -> true; // most actions have no special taking conditions
        startCondition = () -> FAIL; // prevent starting
        onStart = () -> {};
        progressConsumer = delta -> progress += delta;
        finishCondition = () -> progress >= maxProgress;
        onFinish = () -> {};
        reset();
    }

    /**
     * Performs action logic. Changes status.
     */
    public final void perform() {
        if (status == OPEN) { // first execution of perform()
            status = ACTIVE;
            onStart.run();
        }
        progressConsumer.accept(speed);
        if (finishCondition.get()) { // last execution of perform()
            onFinish.run();
            status = COMPLETE;
        }
    }

    public void reset() {
        speed = 1;
        progress = 0;
        maxProgress = 1;
    }

    public ActionConditionStatusEnum addPreAction(Action action) {
        task.addFirstPreAction(action);
        return NEW;
    }
}
