package stonering.objects.jobs.actions.aspects.effect;

import stonering.objects.jobs.actions.Action;

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
