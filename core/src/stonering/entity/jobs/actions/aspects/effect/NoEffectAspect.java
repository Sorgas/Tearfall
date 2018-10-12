package stonering.entity.jobs.actions.aspects.effect;

import stonering.entity.jobs.actions.Action;

/**
 * Effect for moving actor to position.
 *
 * @author Alexander Kuzyakov
 */
public class NoEffectAspect extends EffectAspect{

    public NoEffectAspect(Action action) {
        super(action, 0);
    }

    @Override
    protected void applyEffect() {}
}
