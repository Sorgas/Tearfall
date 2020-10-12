package stonering.entity.job.action.equipment.obtain;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

import stonering.entity.item.Item;
import stonering.entity.job.action.equipment.EquipmentAction;
import stonering.entity.job.action.equipment.use.PutItemToPositionAction;
import stonering.entity.job.action.target.SelfActionTarget;

/**
 * @author Alexander on 7/1/2020
 */
public class GetItemFromInventory extends EquipmentAction {

    protected GetItemFromInventory(Item item) {
        super(new SelfActionTarget());

        // item is in some slot
        startCondition = () -> {
            if (equipment().hauledItem != null)
                return addPreAction(new PutItemToPositionAction(equipment().hauledItem, task.performer.position));
            return validate() && equipment().items.contains(item) ? OK : FAIL;
        };

        onStart = () -> maxProgress = 50;

        onFinish = () -> {
            itemContainer.equippedItemsSystem.removeItemFromEquipment(item);
            equipment().hauledItem = item;
            System.out.println(item + " got from inventory");
        };
    }
}
