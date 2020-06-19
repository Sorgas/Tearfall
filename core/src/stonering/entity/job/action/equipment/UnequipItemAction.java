package stonering.entity.job.action.equipment;

import stonering.entity.job.action.Action;
import stonering.entity.job.action.ActionConditionStatusEnum;
import stonering.entity.job.action.target.ItemActionTarget;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.util.logging.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for unequipping item from unit.
 * Does nothing, if unit doesn't have this item.
 */
public class UnequipItemAction extends Action {
    private Item item;

    public UnequipItemAction(Item item) {
        super(new ItemActionTarget(item));
        this.item = item;
        startCondition = () -> {
            EquipmentAspect equipmentAspect = task.performer.get(EquipmentAspect.class);
            if (equipmentAspect == null) return Logger.TASKS.logError("unit " + task.performer + " has no Equipment Aspect.", FAIL);
            EquipmentSlot slot = equipmentAspect.getSlotWithItem(item);
            if (slot == null) return Logger.TASKS.logError("item " + item + " is not equipped by unit " + task.performer, FAIL);
            if (!slot.canUnequip(item)) {
//                return tryAddUnequipAction(slot.getBlockingItem(item));
            }
            return OK;
        };
        onFinish = () -> {
            //TODO count work amount based on item weight and creature stats
            //TODO implement with slots
            task.performer.get(EquipmentAspect.class).unequipItem(item);
        };
    }

    private ActionConditionStatusEnum tryAddUnequipAction(Item item) {
        UnequipItemAction unequipItemAction = new UnequipItemAction(item);
        task.addFirstPreAction(unequipItemAction);
        return NEW;
    }

    @Override
    public String toString() {
        return "UnequipItemAction for: " + item.title;
    }
}
