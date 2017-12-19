package stonering.objects.jobs.actions.aspects.effect;

import stonering.game.core.model.GameContainer;
import stonering.objects.jobs.actions.Action;

public abstract class EffectAspect {
    protected Action action;
    protected int workAmount;
    protected GameContainer gameContainer;

    public abstract void perform();


}
