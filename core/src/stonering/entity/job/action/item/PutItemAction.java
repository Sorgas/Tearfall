package stonering.entity.job.action.item;

import stonering.entity.item.Item;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.equipment.ObtainItemAction;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Abstract action for putting items to different places.
 * Only defines, that item should be int performer's grab slots.
 * Creates {@link stonering.entity.job.action.equipment.ItemPickupAction}
 *
 * @author Alexander on 12.04.2020
 */
public abstract class PutItemAction extends Action {
    protected Item targetItem;

    protected PutItemAction(ActionTarget target, Item targetItem) {
        super(target);
        this.targetItem = targetItem;

        startCondition = () -> {
            EquipmentAspect equipmentAspect = task.performer.get(EquipmentAspect.class);
            if (equipmentAspect == null) return FAIL; // performer can't carry items
            if (equipmentAspect.items.contains(targetItem)) return OK; // performer already has item
            task.addFirstPreAction(new ObtainItemAction(targetItem));
            System.out.println("pick item action created");
            return NEW;
        };
    }
}
