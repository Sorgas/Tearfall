package stonering.objects.jobs.actions.aspects.effect;

import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.aspects.target.ItemTargetAspect;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.unit.aspects.EquipmentAspect;

/**
 * Created by Alexander on 28.01.2018.
 *
 * equips item to unit
 */
public class EquipItemEffectAspect extends EffectAspect {

    public EquipItemEffectAspect(Action action) {
        super(action);
    }

    @Override
    public void perform() {
        Item item = ((ItemTargetAspect) action.getTargetAspect()).getItem();
        ((EquipmentAspect) action.getPerformer().getAspects().get("equipment")).equipItem(item);
        action.finish();
    }
}
