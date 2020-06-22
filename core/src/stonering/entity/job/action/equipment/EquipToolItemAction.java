package stonering.entity.job.action.equipment;

import stonering.entity.item.Item;
import stonering.entity.job.action.target.SelfActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.util.logging.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for equipping tool items into appropriate slot.
 * Tool can be equipped, if creature has one grab slot.
 * All other tools in grab slots are unequipped.
 * TODO make two handed tools, probably making main-hand and off-hand, and adding comprehensive requirements to tools.
 */
public class EquipToolItemAction extends EquipmentAction {
    private Item item;

    public EquipToolItemAction(Item item) {
        super(new SelfActionTarget());
        this.item = item;

        startCondition = () -> { // check that item is on hands for equipping
            if (!validate()) return FAIL;
            boolean toolEquipped = equipment().grabSlotStream().anyMatch(slot -> slot.grabbedItem == item);
            return toolEquipped
                    ? OK // item is already in some grab slot
                    : addPreAction(new ObtainItemAction(item)); // create action for getting target item 
        };

        onFinish = () -> {
            equipment().grabSlotStream()
                    .filter(slot -> slot.grabbedItem != null && slot.grabbedItem.type.tool != null && slot.grabbedItem != item)
                    .forEach(slot -> {
                        Item wornItem = system.freeGrabSlot(slot); // remove from hands
                        container.onMapItemsSystem.putItem(wornItem, task.performer.position); // put to map
                    });
            equipment().grabSlotStream()
                    .filter(slot -> slot.grabbedItem == null)
                    .findFirst()
                    .ifPresentOrElse(
                            slot -> system.fillGrabSlot(equipment(), slot, item), 
                            () -> Logger.EQUIPMENT.logError("no free slot after removing other tools."));
            //TODO move target item between hands, to maintain main/off hand logic
        };
    }

    protected boolean validate() {
        if(!super.validate()) return false;
        if (item.type.tool == null)
            return Logger.TASKS.logError("Target item is not tool", false);
        return true;
    }

    @Override
    public String toString() {
        return "Tool equipping action: " + item.title;
    }
}
