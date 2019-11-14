package stonering.entity.job.action;

import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.job.action.aspects.ItemPickAction;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.util.geometry.Position;

/**
 * Action for putting item to some target {@link Entity} or position on map. Item will be picked up.
 * Action performer should have {@link EquipmentAspect}.
 * Target entity should have {@link WorkbenchAspect}.
 *
 * Target can be either a position or a container(like chest).
 *
 * @author Alexander on 11.01.2019.
 */
public class PutItemAction extends Action {
    private Item targetItem;
    private Entity targetEntity;
    private Position targetPosition;

    public PutItemAction(Item targetItem, Entity targetEntity) {
        super(new EntityActionTarget(targetEntity, ActionTarget.ANY));
        this.targetItem = targetItem;
        this.targetEntity = targetEntity;
    }

    public PutItemAction(Item targetItem, Position targetPosition) {
        super(new PositionActionTarget(targetPosition, ActionTarget.EXACT));
        this.targetItem = targetItem;
        this.targetPosition = targetPosition;
    }

    @Override
    public void performLogic() {
        EquipmentAspect equipmentAspect = task.performer.getAspect(EquipmentAspect.class);
        equipmentAspect.hauledItems.remove(targetItem);
        if (targetEntity != null) {

            (targetEntity.getAspect(WorkbenchAspect.class)).containedItems.add(targetItem); // put into container
        } else {
            GameMvc.instance().getModel().get(ItemContainer.class).putItem(targetItem, targetPosition); // put on position
        }
    }

    @Override
    public int check() {
        if (targetEntity != null && targetEntity.getAspect(WorkbenchAspect.class) == null) return FAIL;
        EquipmentAspect equipmentAspect = task.performer.getAspect(EquipmentAspect.class);
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
