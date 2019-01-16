package stonering.entity.jobs.actions;

import stonering.entity.jobs.actions.aspects.ItemPickAction;
import stonering.entity.jobs.actions.aspects.target.AspectHolderActionTarget;
import stonering.entity.jobs.actions.aspects.target.ItemActionTarget;
import stonering.entity.jobs.actions.aspects.target.PositionActionTarget;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.aspects.ItemContainerAspect;
import stonering.entity.local.unit.aspects.EquipmentAspect;
import stonering.game.core.model.GameContainer;
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
    private AspectHolder target;
    private Position targetPosition;

    public ItemPutAction(Item targetItem, AspectHolder target) {
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
    public boolean perform() {
        EquipmentAspect equipmentAspect = (EquipmentAspect) task.getPerformer().getAspects().get(EquipmentAspect.NAME);
        equipmentAspect.getHauledItems().remove(targetItem);
        if (target != null) {
            ((ItemContainerAspect) target.getAspects().get(ItemContainerAspect.NAME)).getItems().add(targetItem);
        } else {
            gameMvc.getModel().getItemContainer().putItem(targetItem, targetPosition);
        }
        return true;
    }

    @Override
    public boolean check() {
        if (target != null && !target.getAspects().containsKey(ItemContainerAspect.NAME)) return false;
        EquipmentAspect equipmentAspect = (EquipmentAspect) task.getPerformer().getAspects().get(EquipmentAspect.NAME);
        if (equipmentAspect == null) return false; // performer can carry items
        if (equipmentAspect.getHauledItems().contains(targetItem)) return true; // performer already has item
        return createPickingAction(targetItem);
    }

    private boolean createPickingAction(Item item) {
        task.addFirstPreAction(new ItemPickAction(item));
        return true;
    }

    @Override
    public String toString() {
        return "Item put action: " + targetItem.getTitle();
    }
}
