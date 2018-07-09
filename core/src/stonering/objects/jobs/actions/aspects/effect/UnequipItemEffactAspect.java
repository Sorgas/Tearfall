package stonering.objects.jobs.actions.aspects.effect;

import stonering.objects.jobs.actions.Action;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.unit.aspects.EquipmentAspect;

/**
 * @author Alexander on 08.07.2018.
 */
public class UnequipItemEffactAspect extends EffectAspect {
    private Item item;

    public UnequipItemEffactAspect(Action action, int workAmount) {
        super(action, workAmount);
    }

    @Override
    protected void applyEffect() {
        ((EquipmentAspect) action.getTask().getPerformer().getAspects().get("equipment")).unequipItem(item);
    }
}
