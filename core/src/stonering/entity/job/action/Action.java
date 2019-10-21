package stonering.entity.job.action;

import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.Task;
import stonering.entity.unit.aspects.JobsAspect;
import stonering.util.global.Logger;

/**
 * Action of a unit. Actions are parts of {@link Task}.
 * Actions do some changes in game model(digging, crafting).
 * Actions have target where unit should be to perform action.
 * Actions have requirements and create sub action and add them to task if possible.
 * If action requirements are not met, Action and its task are failed.
 */
public abstract class Action {
    public static final int OK = 1;
    public static final int NEW = 0;
    public static final int FAIL = -1;

    protected String usedSkill;
    public Task task; // can be modified during execution
    public final ActionTarget actionTarget;
    public boolean finished;
    public float workAmount = 1f;
    public float baseSpeed = 0.01f; // distracted from workAmount to make action progress.

    protected Action(ActionTarget actionTarget) {
        this.actionTarget = actionTarget;
        actionTarget.setAction(this);
    }

    /**
     * Returns:
     *    {@value FAIL} if unable perform action or create sub action.
     *    {@value NEW} if new sub action created and added to task.
     *    {@value OK} if checked successfully.
     */
    public abstract int check();

    /**
     * Fetches remaining work amount and performs action.
     * @return true, when action is finished.
     */
    public final boolean perform() {
        float skillModifier = task.performer.getAspect(JobsAspect.class).getSkillModifier(usedSkill);
        if ((workAmount -= baseSpeed * skillModifier) > 0) return false;
        performLogic();
        finish();
        return true;
    }

    /**
     * Applies action logic to model.
     */
    protected abstract void performLogic();

    /**
     * To be called on successful performing.
     */
    protected void finish() {
        finished = true;
        task.finishAction(this);
        task.tryFinishTask();
        Logger.TASKS.logDebug(this + " finished");
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public ActionTarget getActionTarget() {
        return actionTarget;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
