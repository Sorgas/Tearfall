package stonering.objects.jobs.actions.aspects.effect;

import stonering.game.core.model.GameContainer;
import stonering.objects.jobs.actions.Action;

public abstract class EffectAspect {
    protected Action action;
    protected int workAmount;

    public EffectAspect(Action action, int workAmount) {
        this.action = action;
        this.workAmount = workAmount;
    }

    public void perform() {
        workAmount--;
        if(workAmount <= 0) {
            applyEffect();
            action.finish();
        }
    }

    protected abstract void applyEffect();
}
