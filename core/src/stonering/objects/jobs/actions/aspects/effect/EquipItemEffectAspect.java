package stonering.objects.jobs.actions.aspects.effect;

import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.aspects.target.ItemTargetAspect;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.unit.aspects.EquipmentAspect;

/**
 * equips item to unit
 *
 * @author Alexander Kuzyakov on 28.01.2018.
 */
public class EquipItemEffectAspect extends EffectAspect {

    public EquipItemEffectAspect(Action action) {
        super(action, 10);
    }

    @Override
    protected void applyEffect() {
        Item item = ((ItemTargetAspect) action.getTargetAspect()).getItem();
        if (((EquipmentAspect) action.getTask().getPerformer().getAspects().get("equipment")).equipItem(item)) {
            //TODO manage equipped items in item container
            action.getGameContainer().getItemContainer().pickItem(item);
        }
    }
}
