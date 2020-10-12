package stonering.entity.job.action.equipment.use;

import stonering.entity.item.Item;
import stonering.entity.job.action.target.SelfActionTarget;
import stonering.entity.unit.aspects.equipment.GrabEquipmentSlot;

/**
 * Action for equipping tool items into appropriate slot.
 * Tool can be equipped, if creature has one grab slot.
 * All other tools in grab slots are unequipped.
 * TODO make two handed tools, probably making main-hand and off-hand, and adding comprehensive requirements to tools.
 */
public class EquipToolItemAction extends PutItemToDestinationAction {

    public EquipToolItemAction(Item item) {
        super(new SelfActionTarget(), item);

        onFinish = () -> {
            prepareSlotForEquippingTool();
            equipment().grabSlotStream()
                    .filter(GrabEquipmentSlot::isGrabFree)
                    .findFirst()
                    .ifPresent(slot -> system.fillGrabSlot(equipment(), slot, item));
            equipment().hauledItem = null;
            System.out.println(item + " equipped as tool");
            //TODO select one or more hands to maintain main/off hand logic
        };
    }

    private void prepareSlotForEquippingTool() {
        equipment().grabSlotStream() // remove all other tools and drop to map
                .filter(GrabEquipmentSlot::isToolGrabbed)
                .forEach(this::dropGrabbedItemFromSlot);
        if (equipment().grabSlotStream().noneMatch(GrabEquipmentSlot::isGrabFree)) {
            equipment().grabSlotStream().findFirst().ifPresent(this::dropGrabbedItemFromSlot);
        }
    }

    private void dropGrabbedItemFromSlot(GrabEquipmentSlot slot) {
        itemContainer.onMapItemsSystem.addItemToMap(system.freeGrabSlot(slot), task.performer.position);
    }

    protected boolean validate() {
        return super.validate()
                && targetItem.type.tool != null
                && !equipment().grabSlots.isEmpty();
    }
}
