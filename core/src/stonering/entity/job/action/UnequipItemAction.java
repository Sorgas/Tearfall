package stonering.entity.job.action;

import stonering.entity.job.action.target.ItemActionTarget;
import stonering.entity.local.item.Item;
import stonering.entity.local.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.local.unit.aspects.equipment.EquipmentSlot;

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
    public boolean check() {
        EquipmentAspect equipmentAspect = task.getPerformer().getAspect(EquipmentAspect.class);
        if (equipmentAspect == null) return false;

        //item is equipped
        if (!equipmentAspect.getEquippedItems().contains(item)) return true;
        //item is on top
        //TODO move to equipment aspect
        for (EquipmentSlot slot : equipmentAspect.getSlots().values()) {
            if (slot.items.contains(item)) {
                for (int i = slot.items.size() - 1; i >= 0; i--) {
                    if (slot.items.get(i).getType().wear.getLayer() > item.getType().wear.getLayer()) { // slot has item with higher layer.
                        return tryAddUnequipAction(slot.items.get(i));
                    }
                }
            }
        }
        return true;
    }

    private boolean tryAddUnequipAction(Item item) {
        UnequipItemAction unequipItemAction = new UnequipItemAction(item);
        task.addFirstPreAction(unequipItemAction);
        return true;
    }

    @Override
    public void performLogic() {
        //TODO count work amount based on item weight and creature stats
        //TODO implement with slots
        task.getPerformer().getAspect(EquipmentAspect.class).unequipItem(item);
    }

    @Override
    public String toString() {
        return "Unequipping action: " + item.getTitle();
    }
}
