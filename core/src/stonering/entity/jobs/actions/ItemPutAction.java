package stonering.entity.jobs.actions;

import stonering.entity.jobs.actions.aspects.ItemPickAction;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.aspects.ItemContainerAspect;
import stonering.entity.local.unit.aspects.EquipmentAspect;
import stonering.game.core.model.GameContainer;
import stonering.global.utils.Position;

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

    public ItemPutAction(GameContainer gameContainer, Item targetItem, AspectHolder target) {
        super(gameContainer);
        this.targetItem = targetItem;
        this.target = target;
    }

    public ItemPutAction(GameContainer gameContainer, Item targetItem, Position targetPosition) {
        super(gameContainer);
        this.targetItem = targetItem;
        this.targetPosition = targetPosition;
    }

    @Override
    public boolean doLogic() {
        EquipmentAspect equipmentAspect = (EquipmentAspect) task.getPerformer().getAspects().get(EquipmentAspect.NAME);
        equipmentAspect.getHauledItems().remove(targetItem);
        if (target != null) {
            ((ItemContainerAspect) target.getAspects().get(ItemContainerAspect.NAME)).getItems().add(targetItem);
        } else {
            gameContainer.getItemContainer().putItem(targetItem, targetPosition);
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

    @Override
    public Position getTargetPosition() {
        return target != null ? target.getPosition() : targetPosition;
    }

    private boolean createPickingAction(Item item) {
        task.addFirstPreAction(new ItemPickAction(gameContainer, item));
        return true;
    }
}
