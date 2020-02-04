package stonering.entity.job.action;

import stonering.entity.item.aspects.WearAspect;
import stonering.entity.job.action.target.ItemActionTarget;
import stonering.entity.item.Item;
import stonering.entity.job.action.target.ObtainItemAction;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.util.global.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for equipping wear and tool items.
 * Item should be hauled first.
 */
public class EquipItemAction extends Action {
    private Item item;
    private boolean force; //enables unequipping other items.

    public EquipItemAction(Item item, boolean force) {
        super(new ItemActionTarget(item));
        this.item = item;
        this.force = force;

        startCondition = () -> {
            EquipmentAspect equipment = task.performer.getAspect(EquipmentAspect.class);
            if (equipment == null) return Logger.TASKS.logError("unit " + task.performer + " has no Equipment Aspect.", FAIL);
            EquipmentSlot slot = equipment.getSlotForItem(item);
            if (slot == null) return Logger.TASKS.logError("unit " + task.performer + " has no appropriate slots for item " + item, FAIL);
            Item blockingItem = slot.getBlockingItem(item);
            if (blockingItem == null) return OK; // slot is not blocked
            if (!force) return Logger.TASKS.logError("unit " + task.performer + " cannot equip item " + item + " no empty slots.", FAIL);
            if(!equipment.hauledItems.contains(item)) { // take item in hands before equipping
                task.addFirstPreAction(new ObtainItemAction(item));
                return NEW;
            }
            if (item.hasAspect(WearAspect.class)) {
                return createUnequipWearAction(blockingItem); // wear can block only wear items
            } else if (item.isTool()) {
                return createUnequipToolAction(blockingItem);
            }
            return Logger.TASKS.logError("Invalid case in EquipItemAction:check()", FAIL);
        };

        onFinish = () -> {
            ItemContainer container = GameMvc.instance().model().get(ItemContainer.class);
            EquipmentAspect aspect = task.performer.getAspect(EquipmentAspect.class);
            if (!aspect.equipItem(item)) return; // equipping failed
            container.onMapItemsSystem.removeItemFromMap(item);
            container.equippedItemsSystem.itemEquipped(item, aspect);
        };
    }

    /**
     * Creates action for unequipping/equipping high-layer item and adds them to task,
     * so equipping of low-layer item will be in between them.
     * Equipping action will not be performed, if there is no room for item.
     *
     * @param item item(high-layer), that blocks equipping of main item(low-layer)
     * @return false if action are impossible or item is invalid.
     */
    private ActionConditionStatusEnum createUnequipWearAction(Item item) {
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
    private ActionConditionStatusEnum createUnequipToolAction(Item item) {
        UnequipItemAction unequipItemAction = new UnequipItemAction(item);
        task.addFirstPreAction(unequipItemAction);
        return NEW;
    }

    @Override
    public String toString() {
        return "Equipping action: " + item.title;
    }
}
