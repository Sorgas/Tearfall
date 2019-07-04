package stonering.entity.job.action;

import stonering.entity.job.action.aspects.ItemPickAction;
import stonering.entity.job.action.target.AspectHolderActionTarget;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.lists.ItemContainer;
import stonering.util.geometry.Position;

/**
 * Action for putting item to some target. Item will be picked up.
 * Action performer should have {@link EquipmentAspect}
 * Target should have {@link ItemContainerAspect}
 *
 * @author Alexander on 11.01.2019.
 */
public class ItemPutAction extends Action {
    private Item targetItem;
    private Entity target;
    private Position targetPosition;

    public ItemPutAction(Item targetItem, Entity target) {
        super(new AspectHolderActionTarget(target, true, true));
        this.targetItem = targetItem;
        this.target = target;
    }

    public ItemPutAction(Item targetItem, Position targetPosition) {
        super(new PositionActionTarget(targetPosition, true, false));
        this.targetItem = targetItem;
        this.targetPosition = targetPosition;
    }

    @Override
    public void performLogic() {
        EquipmentAspect equipmentAspect = task.getPerformer().getAspect(EquipmentAspect.class);
        equipmentAspect.getHauledItems().remove(targetItem);
        if (target != null) {
            (target.getAspect(ItemContainerAspect.class)).getItems().add(targetItem);
        } else {
            GameMvc.instance().getModel().get(ItemContainer.class).putItem(targetItem, targetPosition);
        }
    }

    @Override
    public boolean check() {
        if (target != null && target.getAspect(ItemContainerAspect.class) != null) return false;
        EquipmentAspect equipmentAspect = task.getPerformer().getAspect(EquipmentAspect.class);
        if (equipmentAspect == null) return false; // performer can carry item
        if (equipmentAspect.getHauledItems().contains(targetItem)) return true; // performer already has item
        return createPickingAction(targetItem);
    }

    private boolean createPickingAction(Item item) {
        task.addFirstPreAction(new ItemPickAction(item));
        return true;
    }

    @Override
    public String toString() {
        return "Item put name: " + targetItem.getTitle();
    }
}
