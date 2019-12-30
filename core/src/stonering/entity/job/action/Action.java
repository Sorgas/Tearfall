package stonering.entity.job.action;

import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.Task;
import stonering.entity.unit.aspects.JobsAspect;

/**
 * Action of a unit. Actions are parts of {@link Task}.
 * Actions do some changes in game model(digging, crafting).
 * Actions have target where unit should be to perform action.
 * Actions have requirements and create sub action and add them to task if possible.
 * If action requirements are not met, Action and its task are failed.
 * <p>
 */
public abstract class Action {
    protected String usedSkill;
    public Task task; // can be modified during execution
    public final ActionTarget actionTarget;
    public float workAmount = 1f;
    public float baseSpeed = 0.01f; // distracted from workAmount to make action progress.

    protected Action(ActionTarget actionTarget) {
        this.actionTarget = actionTarget;
        actionTarget.setAction(this);
    }

    public abstract ActionConditionStatusEnum check();

    /**
     * Fetches remaining work amount and performs action.
     *
     * @return true, when action is finished.
     */
    public final boolean perform() {
        applyWork();
        if(!isFinished()) return false;
        performLogic();
        task.finishAction(this);
        return true;
    }

    public boolean isFinished() {
        return workAmount <= 0;
    }

    protected void applyWork() {
        workAmount -= getWorkDelta();
    }

    /**
     * Count delta for left work amount on one tick. Higher values means faster work.
     * TODO different work amount for action. consider stats when counting speed. add experience.
     */
    protected float getWorkDelta() {
        float skillModifier = task.performer.getAspect(JobsAspect.class).getSkillModifier(usedSkill);
        return baseSpeed * skillModifier;
    }

    /**
     * Applies action logic to model.
     */
    protected abstract void performLogic();

    /**
     * Resets task state as it had not been started.
     */
    public void reset() {
        workAmount = 1f;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
