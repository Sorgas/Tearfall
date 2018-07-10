package stonering.objects.jobs.actions.aspects.effect;

import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.aspects.target.ItemTargetAspect;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.unit.aspects.EquipmentAspect;

/**
 * @author Alexander on 08.07.2018.
 */
public class PickUpItemEffectAspect extends EffectAspect {

    public PickUpItemEffectAspect(Action action) {
        super(action, 10);
    }

    @Override
    protected void applyEffect() {
        Item item = ((ItemTargetAspect) action.getTargetAspect()).getItem();
        ((EquipmentAspect) action.getPerformer().getAspects().get("equipment")).equipItem(item, true);
    }
}
