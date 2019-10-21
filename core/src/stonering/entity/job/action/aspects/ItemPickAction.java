package stonering.entity.job.action.aspects;

import stonering.entity.job.action.Action;
import stonering.entity.job.action.target.ItemActionTarget;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.items.ItemContainer;
import stonering.util.global.Logger;

/**
 * Action for picking and hauling item. Performer should have {@link EquipmentAspect}.
 * Item should be on the ground.
 *
 * @author Alexander on 12.01.2019.
 */
public class ItemPickAction extends Action {
    private Item item;

    public ItemPickAction(Item targetItem) {
        super(new ItemActionTarget(targetItem));
        item = targetItem;
    }

    @Override
    public void performLogic() {
        Item targetItem = getTargetItem();
        Logger.TASKS.logDebug("Picking item " + targetItem.getTitle());
        task.performer.getAspect(EquipmentAspect.class).pickupItem(targetItem);
        GameMvc.instance().getModel().get(ItemContainer.class).pickItem(targetItem);
    }

    @Override
    public int check() {
        Logger.TASKS.logDebug("Checking picking action");
        if (task.performer.getAspect(EquipmentAspect.class) == null) return FAIL; // performer cannot pick up item
        if (GameMvc.instance().getModel().get(ItemContainer.class).itemMap.get(item.position).contains(item)) return OK;
        return FAIL;
    }

    private Item getTargetItem() {
        return ((ItemActionTarget) actionTarget).getItem();
    }
}
