package stonering.entity.jobs.actions.aspects.effect;

import stonering.entity.jobs.actions.Action;
import stonering.entity.local.unit.aspects.PlanningAspect;

public abstract class EffectAspect {
    protected Action action;
    protected int workAmount;

    public EffectAspect(Action action, int workAmount) {
        this.action = action;
        this.workAmount = workAmount;
    }

    /**
     * {@link PlanningAspect} should guarantee all action checks before calling this.
     *
     * @return Whether action finished or not after this.
     */
    public boolean perform() {
        if(workAmount <= 0) {
            applyEffect();
            action.finish();
            return true;
        } else {
            workAmount--;
            return false;
        }
    }

    protected abstract void applyEffect();
}
