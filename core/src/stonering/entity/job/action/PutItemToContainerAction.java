package stonering.entity.job.action;

import stonering.entity.job.action.aspects.ItemPickAction;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.ItemContainer;
import stonering.util.geometry.Position;

/**
 * Action for putting item to some targetEntity. Item will be picked up.
 * Action performer should have {@link EquipmentAspect}.
 * Target should have {@link ItemContainerAspect}.
 *
 * Target can be either a position or a container(like chest).
 *
 * @author Alexander on 11.01.2019.
 */
public class PutItemToContainerAction extends Action {
    private Item targetItem;
    private Entity targetEntity;
    private Position targetPosition;

    public PutItemToContainerAction(Item targetItem, Entity targetEntity) {
        super(new EntityActionTarget(targetEntity, ActionTarget.ANY));
        this.targetItem = targetItem;
        this.targetEntity = targetEntity;
    }

    public PutItemToContainerAction(Item targetItem, Position targetPosition) {
        super(new PositionActionTarget(targetPosition, ActionTarget.EXACT));
        this.targetItem = targetItem;
        this.targetPosition = targetPosition;
    }

    @Override
    public void performLogic() {
        EquipmentAspect equipmentAspect = task.getPerformer().getAspect(EquipmentAspect.class);
        equipmentAspect.hauledItems.remove(targetItem);
        if (targetEntity != null) {
            (targetEntity.getAspect(ItemContainerAspect.class)).items.add(targetItem); // put into container
        } else {
            GameMvc.instance().getModel().get(ItemContainer.class).putItem(targetItem, targetPosition); // put on position
        }
    }

    @Override
    public int check() {
        if (targetEntity != null && targetEntity.getAspect(ItemContainerAspect.class) != null) return FAIL;
        EquipmentAspect equipmentAspect = task.getPerformer().getAspect(EquipmentAspect.class);
        if (equipmentAspect == null) return FAIL; // performer can't carry items
        if (equipmentAspect.hauledItems.contains(targetItem)) return OK; // performer already has item
        return createPickingAction(targetItem);
    }

    private int createPickingAction(Item item) {
        task.addFirstPreAction(new ItemPickAction(item));
        return NEW;
    }

    @Override
    public String toString() {
        return "Item put name: " + targetItem.getTitle();
    }
}
