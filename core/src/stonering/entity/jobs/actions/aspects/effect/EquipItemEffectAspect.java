package stonering.entity.jobs.actions.aspects.effect;

import stonering.entity.jobs.actions.Action;
import stonering.entity.jobs.actions.aspects.target.ItemActionTarget;
import stonering.entity.local.items.Item;
import stonering.entity.local.unit.aspects.EquipmentAspect;
import stonering.util.global.TagLoggersEnum;

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
        Item item = ((ItemActionTarget) action.getTargetAspect()).getItem();
        if (((EquipmentAspect) action.getTask().getPerformer().getAspects().get("equipment")).equipItem(item)) {

            //TODO manage equipped items in item container
            action.getGameContainer().getItemContainer().pickItem(item);
        }
    }
}
