package stonering.entity.job.action;

import stonering.entity.job.action.target.ItemActionTarget;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.exceptions.NotSuitableItemException;
import stonering.game.GameMvc;
import stonering.game.model.lists.ItemContainer;
import stonering.util.global.Logger;

public class EquipItemAction extends Action {
    private Item item;
    private boolean force; //enables unequipping other item.

    public EquipItemAction(Item item, boolean force) {
        super(new ItemActionTarget(item));
        this.item = item;
        this.force = force;
    }


    @Override
    protected void performLogic() {
        //TODO manage equipped item in item container
        if ((task.getPerformer().getAspect(EquipmentAspect.class)).equipItem(item))
            GameMvc.instance().getModel().get(ItemContainer.class).pickItem(item);
    }

    @Override
    public int check() {
        try {
            EquipmentAspect equipmentAspect = task.getPerformer().getAspect(EquipmentAspect.class);
            Item blockingItem = equipmentAspect.checkItemForEquip(this.item);
            if (blockingItem == null || !force) return OK; // do not unequip if not forced.
            if (item.isWear()) {
                return createUnequipWearAction(blockingItem);
            } else if (item.isTool()) {
                return createUnequipToolAction(blockingItem);
            }
            Logger.ITEMS.logError("Invalid case in EquipItemAction:check()");
            return FAIL;
        } catch (NotSuitableItemException e) {
            Logger.ITEMS.logError(task.getPerformer().toString() + " tried to equip not tool or wear item " + item.toString() + " .");
            return FAIL;
        }
    }

    /**
     * Creates action for unequipping/equipping high-layer item and adds them to task,
     * so equipping of low-layer item will be in between them.
     * Equipping action will not be performed, if there is no room for item.
     *
     * @param item item(high-layer), that blocks equipping of main item(low-layer)
     * @return false if action are impossible or item is invalid.
     */
    private int createUnequipWearAction(Item item) {
        // unequip action
        UnequipItemAction action = new UnequipItemAction(item);
        task.addFirstPreAction(action);

        // equip action
        EquipItemAction equipItemAction = new EquipItemAction(item, false);
        task.addFirstPreAction(equipItemAction);
        return NEW;
    }

    /**
     * Creates action only for unequipping item. Used to unequip tool when equipping another tool, as tools are highest layer,
     * and two tools cannot be held simultaneously.
     */
    private int createUnequipToolAction(Item item) {
        UnequipItemAction unequipItemAction = new UnequipItemAction(item);
        task.addFirstPreAction(unequipItemAction);
        return NEW;
    }

    @Override
    public String toString() {
        return "Equipping action: " + item.getTitle();
    }
}
