package stonering.entity.job.action.equipment;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.WearAspect;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.target.SelfActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.entity.unit.aspects.equipment.GrabEquipmentSlot;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.CreatureEquipmentSystem;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.logging.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for equipping wear items.
 *
 * @author Alexander
 */
public class EquipWearItemAction extends Action {
    public Item item;

    public EquipWearItemAction(Item item) {
        super(new SelfActionTarget());
        this.item = item;
        CreatureEquipmentSystem system = GameMvc.model().get(UnitContainer.class).equipmentSystem;
        ItemContainer itemContainer = GameMvc.model().get(ItemContainer.class);
        WearAspect wear = item.get(WearAspect.class);

        startCondition = () -> {
            EquipmentAspect equipment = task.performer.get(EquipmentAspect.class);
            if (wear == null) return Logger.TASKS.logError("Target item is not wear", FAIL);
            if (equipment == null)
                return Logger.TASKS.logError("unit " + task.performer + " has no Equipment Aspect.", FAIL);
            if (equipment.slots.get(wear.slot) == null)
                return Logger.TASKS.logError("unit " + task.performer + " has no appropriate slots for item " + item, FAIL);
            if (!equipment.isItemInGrabSlots(item)) { // pick up needed item
                task.addFirstPreAction(new ObtainItemAction(item));
                return NEW;
            }
            return OK;
        };

        onFinish = () -> {
            EquipmentAspect equipment = task.performer.get(EquipmentAspect.class);
            EquipmentSlot targetSlot = equipment.slots.get(wear.slot);

            Item previousItem = system.freeSlot(targetSlot);
            if(previousItem != null) itemContainer.onMapItemsSystem.putItem(previousItem, task.performer.position);// drop previous item

            GrabEquipmentSlot grabSlot = equipment.grabSlots.values().stream().filter(slot -> slot.grabbedItem == item).findFirst().orElse(null); // should never be null
            if (grabSlot != null) {
                system.freeGrabSlot(grabSlot); // free hand
                system.fillSlot(equipment, targetSlot, item); // put on wear
                return;
            }
            Logger.EQUIPMENT.logError("Target item is not in hands before equipping.");
        };
    }

    @Override
    public String toString() {
        return "Equipping wear item " + item.title + " action: ";
    }
}
