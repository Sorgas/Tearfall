package stonering.entity.job.action.equipment;

import stonering.entity.item.Item;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.ItemAction;
import stonering.entity.job.action.target.SelfActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

/**
 * Fictive action to handle different cases of item placement.
 * Checks item placement and creates corresponding pre-action. (pick up from ground, get from container).
 *
 * @author Alexander on 04.02.2020.
 */
public class ObtainItemAction extends ItemAction {

    public ObtainItemAction(Item item) {
        super(new SelfActionTarget());
        startCondition = () -> {
            if(task.performer.get(EquipmentAspect.class).items.contains(item)) return OK;
            if (container.equippedItemsSystem.isItemEquipped(item)) return FAIL; // item is equipped on another unit
            Action action = container.containedItemsSystem.isItemContained(item) 
                    ? new GetItemFromContainerAction(item, container.contained.get(item).entity) // pickup from ground 
                    : new ItemPickupAction(item); // take from container
            return addPreAction(action);
        };
    }
}
