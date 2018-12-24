package stonering.entity.jobs.actions.aspects.effect;

import stonering.entity.jobs.actions.Action;
import stonering.entity.local.crafting.ItemOrder;

/**
 * @author Alexander on 24.12.2018.
 */
public class WorkbenchItemOrderEffectAspect extends EffectAspect {
    ItemOrder order;

    public WorkbenchItemOrderEffectAspect(Action action, int workAmount, ItemOrder order) {
        super(action, workAmount);
        this.order = order;
    }


    @Override
    protected void applyEffect() {

    }
}
