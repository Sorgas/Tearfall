package stonering.entity.job.action.equipment;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.WearAspect;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.target.SelfActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.game.GameMvc;
import stonering.game.model.system.unit.CreatureEquipmentSystem;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.global.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for equipping wear items.
 *
 * @author Alexander
 */
public class EquipWearItemAction extends Action {
    private Item item;

    public EquipWearItemAction(Item item) {
        super(new SelfActionTarget());
        this.item = item;
        CreatureEquipmentSystem system = GameMvc.model().get(UnitContainer.class).equipmentSystem;
        WearAspect wear = item.getAspect(WearAspect.class);

        startCondition = () -> {
            EquipmentAspect equipment = task.performer.getAspect(EquipmentAspect.class);
            if (wear == null) return Logger.TASKS.logError("Target item is not wear", FAIL);
            if (equipment == null)
                return Logger.TASKS.logError("unit " + task.performer + " has no Equipment Aspect.", FAIL);
            EquipmentSlot slot = system.getSlotForEquippingWear(equipment, item);
            if (slot == null)
                return Logger.TASKS.logError("unit " + task.performer + " has no appropriate slots for item " + item, FAIL);
            if (!equipment.isItemInGrabSlots(item)) { // pick up needed item
                task.addFirstPreAction(new ObtainItemAction(item));
                return NEW;
            }
            if (slot.item == null) return OK; // slot is free
            task.addFirstPreAction(new UnequipItemAction(slot.item)); // if other wear blocks equipping
            return NEW;
        };

        onFinish = () -> {
            system.equipWearItem(task.performer.getAspect(EquipmentAspect.class), item);
        };
    }

    @Override
    public String toString() {
        return "Equipping wear item " + item.title + " action: ";
    }
}
