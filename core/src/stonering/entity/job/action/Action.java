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
    protected Task task; // can be modified during execution
    protected ActionTarget actionTarget;
    protected boolean finished;
    protected String usedSkill;
    private float workAmount = 1f;
    private float baseSpeed = 0.01f; // distracted from workAmount to make action progress.

    protected Action(ActionTarget actionTarget) {
        this.actionTarget = actionTarget;
        actionTarget.setAction(this);
    }

    /**
     * Returns {@value FAIL} if unable perform action or create sub action.
     * Task can have more actions  after this.
     */
    public abstract int check();

    /**
     * Fetches remaining work amount and performs action.
     * @return true, when action is finished.
     */
    public final boolean perform() {
        float skillModifier = task.getPerformer().getAspect(JobsAspect.class).getSkillModifier(usedSkill);
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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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
}
