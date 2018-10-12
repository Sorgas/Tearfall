package stonering.entity.jobs.actions.aspects.effect;

import stonering.entity.jobs.actions.Action;
import stonering.entity.local.items.Item;
import stonering.entity.local.unit.aspects.EquipmentAspect;

/**
 * @author Alexander on 08.07.2018.
 */
public class UnequipItemEffectAspect extends EffectAspect {
    private Item item;

    public UnequipItemEffectAspect(Action action, int workAmount) {
        super(action, workAmount);
    }

    @Override
    protected void applyEffect() {
        //TODO implement with slots
        ((EquipmentAspect) action.getTask().getPerformer().getAspects().get("equipment")).unequipItem(item);
    }
}
