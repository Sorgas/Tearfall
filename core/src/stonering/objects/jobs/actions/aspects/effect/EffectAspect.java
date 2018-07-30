package stonering.objects.jobs.actions.aspects.effect;

import stonering.objects.jobs.actions.Action;

public abstract class EffectAspect {
    protected Action action;
    protected int workAmount;

    public EffectAspect(Action action, int workAmount) {
        this.action = action;
        this.workAmount = workAmount;
    }

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
