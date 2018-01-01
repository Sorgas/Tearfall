package stonering.objects.jobs.actions.aspects.effect;

import stonering.game.core.model.GameContainer;
import stonering.objects.jobs.actions.Action;

public abstract class EffectAspect {
    protected Action action;
    protected int workAmount;
    protected GameContainer gameContainer;

    public EffectAspect(Action action, GameContainer gameContainer) {
        this.action = action;
        this.gameContainer = gameContainer;
    }

    public void perform() {
        action.finish();
    }
}
