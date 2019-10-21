package stonering.entity.job.action;

import stonering.entity.job.action.target.ItemActionTarget;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.util.global.Logger;

/**
 * Action for unequipping item from unit.
 * Does nothing, if unit doesn't have this item.
 */
public class UnequipItemAction extends Action {
    private Item item;

    public UnequipItemAction(Item item) {
        super(new ItemActionTarget(item));
        this.item = item;
    }

    @Override
    public int check() {
        EquipmentAspect equipmentAspect = task.performer.getAspect(EquipmentAspect.class);
        if (equipmentAspect == null) return failWithLog("unit " + task.performer + " has no Equipment Aspect.");
        EquipmentSlot slot = equipmentAspect.getSlotWithItem(item);
        if (slot == null) return failWithLog("item " + item + " is not equipped by unit " + task.performer);
        if (!slot.canUnequip(item)) {
            return tryAddUnequipAction(slot.getBlockingItem(item));
        }
        return OK;
    }

    private int tryAddUnequipAction(Item item) {
        UnequipItemAction unequipItemAction = new UnequipItemAction(item);
        task.addFirstPreAction(unequipItemAction);
        return NEW;
    }

    @Override
    public void performLogic() {
        //TODO count work amount based on item weight and creature stats
        //TODO implement with slots
        task.performer.getAspect(EquipmentAspect.class).unequipItem(item);
    }

    @Override
    public String toString() {
        return "Unequipping name: " + item.getTitle();
    }

    private int failWithLog(String message) {
        Logger.ITEMS.logError(message);
        return FAIL;
    }
}
