package stonering.entity.job.action.aspects;

import stonering.entity.job.action.Action;
import stonering.entity.job.action.ActionConditionStatusEnum;
import stonering.entity.job.action.target.ItemActionTarget;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.util.global.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

/**
 * Action for picking and hauling item. Performer should have {@link EquipmentAspect}.
 * Item should be on the ground.
 *
 * @author Alexander on 12.01.2019.
 */
public class ItemPickupAction extends Action {
    private Item item;

    public ItemPickupAction(Item targetItem) {
        super(new ItemActionTarget(targetItem));
        item = targetItem;
    }

    @Override
    public void performLogic() {
        Item targetItem = getTargetItem();
        EquipmentAspect equipment = task.performer.getAspect(EquipmentAspect.class);
        ItemContainer container = GameMvc.instance().model().get(ItemContainer.class);

        container.onMapItemsSystem.removeItemFromMap(targetItem);
        equipment.pickupItem(targetItem);
        container.equippedItemsSystem.itemEquipped(item, equipment);
    }

    @Override
    public ActionConditionStatusEnum check() {
        Logger.TASKS.logDebug("Checking picking action");
        if (task.performer.getAspect(EquipmentAspect.class) == null) return FAIL; // performer cannot pick up item
        if (GameMvc.instance().model().get(ItemContainer.class).itemMap.get(item.position).contains(item)) return OK;
        return FAIL;
    }

    private Item getTargetItem() {
        return ((ItemActionTarget) actionTarget).getItem();
    }
}
