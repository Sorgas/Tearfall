package stonering.entity.job.action.equipment.use;

import stonering.entity.item.Item;
import stonering.entity.job.action.equipment.EquipmentAction;
import stonering.entity.job.action.equipment.obtain.ObtainItemAction;
import stonering.entity.job.action.target.ActionTarget;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Abstract action for putting items to different places like ground, containers, or equipment.
 * Only defines, that item should be in performer's buffer.
 *
 * @author Alexander on 12.04.2020
 */
public abstract class PutItemToDestinationAction extends EquipmentAction {
    public Item targetItem;

    protected PutItemToDestinationAction(ActionTarget target, Item targetItem) {
        super(target);
        this.targetItem = targetItem;

        startCondition = () -> {
            if (!validate()) return FAIL;
            if (equipment().hauledItem == targetItem) return OK; // performer has item
            return addPreAction(new ObtainItemAction(targetItem));
        };

        onStart = () -> maxProgress = 20;
    }
}
