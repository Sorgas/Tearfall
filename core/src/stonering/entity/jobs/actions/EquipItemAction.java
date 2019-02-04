package stonering.entity.jobs.actions;

import stonering.entity.jobs.actions.target.ItemActionTarget;
import stonering.entity.local.items.Item;
import stonering.entity.local.unit.aspects.EquipmentAspect;
import stonering.exceptions.NotSuitableItemException;
import stonering.game.core.model.lists.ItemContainer;
import stonering.util.global.TagLoggersEnum;

public class EquipItemAction extends Action {
    private Item item;
    private boolean force; //enables unequipping other items.

    public EquipItemAction(Item item, boolean force) {
        super(new ItemActionTarget(item));
        this.item = item;
        this.force = force;
    }


    @Override
    protected void performLogic() {
        //TODO manage equipped items in item container
        if (((EquipmentAspect) task.getPerformer().getAspects().get("equipment")).equipItem(item))
            gameMvc.getModel().get(ItemContainer.class).pickItem(item);
    }

    @Override
    public boolean check() {
        try {
            EquipmentAspect equipmentAspect = (EquipmentAspect) task.getPerformer().getAspects().get("equipment");
            Item blockingItem = equipmentAspect.checkItemForEquip(this.item);
            if (blockingItem == null) return true;
            if (!force) return true; // do not unequip if not forced.
            if (item.isWear()) {
                return createUnequipWearAction(blockingItem);
            } else if (item.isTool()) {
                return createUnequipToolAction(blockingItem);
            }
            TagLoggersEnum.ITEMS.logError("Invalid case in EquipItemAction:check()");
            return false;
        } catch (NotSuitableItemException e) {
            TagLoggersEnum.ITEMS.logError(task.getPerformer().toString() + " tried to equip not tool or wear item " + item.toString() + " .");
            return false;
        }
    }

    /**
     * Creates actions for unequipping/equipping high-layer item and adds them to task,
     * so equipping of low-layer item will be in between them.
     * Equipping action will not be performed, if there is no room for item.
     *
     * @param item item(high-layer), that blocks equipping of main item(low-layer)
     * @return false if actions are impossible or item is invalid.
     */
    private boolean createUnequipWearAction(Item item) {
        // unequip action
        UnequipItemAction action = new UnequipItemAction(item);
        task.addFirstPreAction(action);

        // equip action
        EquipItemAction equipItemAction = new EquipItemAction(item, false);
        task.addFirstPreAction(action);
        return true;
    }

    /**
     * Creates action only for unequipping item. Used to unequip tool when equipping another tool, as tools are highest layer,
     * and two tools cannot be held simultaneously.
     *
     * @param item unequipping tool.
     * @return
     */
    private boolean createUnequipToolAction(Item item) {
        UnequipItemAction unequipItemAction = new UnequipItemAction(item);
        task.addFirstPreAction(unequipItemAction);
        return true;
    }

    @Override
    public String toString() {
        return "Equipping action: " + item.getTitle();
    }
}
