package stonering.entity.jobs.actions.aspects.effect;

import stonering.entity.jobs.actions.Action;
import stonering.entity.jobs.actions.aspects.target.ItemActionTarget;
import stonering.entity.local.items.Item;
import stonering.entity.local.unit.aspects.EquipmentAspect;

/**
 * @author Alexander on 08.07.2018.
 */
public class PickUpItemEffectAspect extends EffectAspect {

    public PickUpItemEffectAspect(Action action) {
        super(action, 10);
    }

    @Override
    protected void applyEffect() {
        Item item = ((ItemActionTarget) action.getTargetAspect()).getItem();
        ((EquipmentAspect) action.getTask().getPerformer().getAspects().get("equipment")).pickupItem(item);
        action.getGameContainer().getItemContainer().removeItem(item);
    }
}
