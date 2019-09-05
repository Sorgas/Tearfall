package stonering.entity.job.action.aspects;

import stonering.entity.job.action.Action;
import stonering.entity.job.action.target.ItemActionTarget;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.ItemContainer;
import stonering.util.global.Logger;

/**
 * Action for picking and hauling item. Performer should have {@link EquipmentAspect}
 *
 * @author Alexander on 12.01.2019.
 */
public class ItemPickAction extends Action {

    public ItemPickAction(Item targetItem) {
        super(new ItemActionTarget(targetItem));
    }

    @Override
    public void performLogic() {
        Item targetItem = getTargetItem();
        Logger.TASKS.logDebug("Picking item " + targetItem.getTitle());
        task.getPerformer().getAspect(EquipmentAspect.class).pickupItem(targetItem);
        GameMvc.instance().getModel().get(ItemContainer.class).pickItem(targetItem);
    }

    @Override
    public int check() {
        Logger.TASKS.logDebug("Checking picking action");
        if (task.getPerformer().getAspect(EquipmentAspect.class) == null) return FAIL;
        if(GameMvc.instance().getModel().get(ItemContainer.class).checkItem(getTargetItem())) return OK; // no item on map
        return FAIL;
    }

    private Item getTargetItem() {
        return ((ItemActionTarget) actionTarget).getItem();
    }
}
