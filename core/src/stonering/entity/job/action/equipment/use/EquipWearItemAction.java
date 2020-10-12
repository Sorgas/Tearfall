package stonering.entity.job.action.equipment.use;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.WearAspect;
import stonering.entity.job.action.target.SelfActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;

/**
 * Action for equipping wear items.
 *
 * @author Alexander
 */
public class EquipWearItemAction extends PutItemToDestinationAction {

    public EquipWearItemAction(Item targetItem) {
        super(new SelfActionTarget(), targetItem);

        onFinish = () -> {
            WearAspect wear = targetItem.get(WearAspect.class);
            EquipmentSlot targetSlot = equipment().slots.get(wear.slot);

            // TODO wrap with layer condition
            Item previousItem = system.freeSlot(targetSlot);
            itemContainer.onMapItemsSystem.addItemToMap(previousItem, task.performer.position);

            targetSlot.item = targetItem;
            equipment().hauledItem = null;
        };
    }

    protected boolean validate() {
        WearAspect wear = targetItem.get(WearAspect.class);
        return super.validate()
                && wear != null
                && equipment().slots.get(wear.slot) != null;
    }
}
