package stonering.entity.jobs.actions.aspects;

import stonering.entity.jobs.actions.Action;
import stonering.entity.local.items.Item;
import stonering.entity.local.unit.aspects.EquipmentAspect;

/**
 * Action for picking and hauling item. Performer should have {@link EquipmentAspect}
 *
 * @author Alexander on 12.01.2019.
 */
public class ItemPickAction extends Action {
    private Item targetItem;

    public ItemPickAction(Item targetItem) {
        super();
        this.targetItem = targetItem;
    }

    @Override
    public boolean perform() {
        ((EquipmentAspect) task.getPerformer().getAspects().get(EquipmentAspect.NAME)).pickupItem(targetItem);
        return true;
    }

    @Override
    public boolean check() {
        if (!task.getPerformer().getAspects().containsKey(EquipmentAspect.NAME)) return false;
        return gameMvc.getModel().getItemContainer().checkItem(targetItem);
    }
}
