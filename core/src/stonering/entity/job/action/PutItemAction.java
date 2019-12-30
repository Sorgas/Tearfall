package stonering.entity.job.action;

import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.job.action.aspects.ItemPickupAction;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.util.geometry.Position;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for putting item to some target {@link Entity} or position on map. Item will be picked up.
 * Action performer should have {@link EquipmentAspect}.
 * Target entity should have {@link WorkbenchAspect}.
 * <p>
 * Target can be either a position or a container(like chest).
 *
 * @author Alexander on 11.01.2019.
 */
public class PutItemAction extends Action {
    private Item targetItem;
    private Entity targetEntity;
    private Position targetPosition;

    public PutItemAction(Item targetItem, Entity targetEntity) {
        super(new EntityActionTarget(targetEntity, ActionTargetTypeEnum.ANY));
        this.targetItem = targetItem;
        this.targetEntity = targetEntity;
        startCondition = () -> {
            if (targetEntity != null && targetEntity.getAspect(WorkbenchAspect.class) == null) return FAIL;
            EquipmentAspect equipmentAspect = task.performer.getAspect(EquipmentAspect.class);
            if (equipmentAspect == null) return FAIL; // performer can't carry items
            if (equipmentAspect.hauledItems.contains(targetItem)) return OK; // performer already has item
            return createPickingAction(targetItem);
        };

        onFinish = () -> {
            EquipmentAspect equipmentAspect = task.performer.getAspect(EquipmentAspect.class);
            ItemContainer container = GameMvc.instance().model().get(ItemContainer.class);
            equipmentAspect.hauledItems.remove(targetItem);
            container.equippedItemsSystem.itemUnequipped(targetItem);
            if (targetEntity != null) { // put into wb
                container.containedItemsSystem.addItemToWorkbench(targetItem, targetEntity.getAspect(WorkbenchAspect.class));
            } else { // put on position
                container.onMapItemsSystem.putItem(targetItem, targetPosition);
            }
        };
    }

    public PutItemAction(Item targetItem, Position targetPosition) {
        super(new PositionActionTarget(targetPosition, ActionTargetTypeEnum.EXACT));
        this.targetItem = targetItem;
        this.targetPosition = targetPosition;
    }

    private ActionConditionStatusEnum createPickingAction(Item item) {
        task.addFirstPreAction(new ItemPickupAction(item));
        return NEW;
    }

    @Override
    public String toString() {
        return "Item put name: " + targetItem.getTitle();
    }
}
