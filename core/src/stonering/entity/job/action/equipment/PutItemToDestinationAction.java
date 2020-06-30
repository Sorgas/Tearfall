package stonering.entity.job.action.equipment;

import stonering.entity.item.Item;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Abstract action for putting items to different places like ground, containers, or equipment.
 * Only defines, that item should be in performer's buffer.
 *
 * @author Alexander on 12.04.2020
 */
public abstract class PutItemToDestinationAction extends EquipmentAction {
    protected Item targetItem;

    protected PutItemToDestinationAction(ActionTarget target, Item targetItem) {
        super(target);
        this.targetItem = targetItem;

        startCondition = () -> {
            if (!validate()) return FAIL;
            if (equipment().itemBuffer == targetItem) return OK; // performer has item
            return addPreAction(new ObtainItemAction(targetItem));
        };
    }
}
