package stonering.entity.job.action.aspects;

import stonering.entity.job.action.Action;
import stonering.entity.job.action.target.ItemActionTarget;
import stonering.entity.local.item.Item;
import stonering.entity.local.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.lists.ItemContainer;

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
        task.getPerformer().getAspect(EquipmentAspect.class).pickupItem(targetItem);
        GameMvc.instance().getModel().get(ItemContainer.class).pickItem(targetItem);
    }

    @Override
    public boolean check() {
        if (task.getPerformer().getAspect(EquipmentAspect.class) == null) return false;
        return GameMvc.instance().getModel().get(ItemContainer.class).checkItem(getTargetItem());
    }

    private Item getTargetItem() {
        return ((ItemActionTarget) actionTarget).getItem();
    }
}
