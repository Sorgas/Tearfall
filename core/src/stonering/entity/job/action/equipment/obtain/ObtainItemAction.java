package stonering.entity.job.action.equipment.obtain;

import stonering.entity.item.Item;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.ItemAction;
import stonering.entity.job.action.equipment.EquipmentAction;
import stonering.entity.job.action.target.SelfActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.util.logging.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

/**
 * Fictive action to handle different cases of item placement.
 * Item can be got from ground, container, or performers equipment. All these cases are handled in separate pre-actions.
 * Target item is put to {@link EquipmentAspect#itemBuffer} for use in another action(see )
 *
 * @author Alexander on 04.02.2020.
 */
public class ObtainItemAction extends EquipmentAction {
    protected Item targetItem;

    public ObtainItemAction(Item item) {
        super(new SelfActionTarget());
        targetItem = item;
        startCondition = () -> {
            if(equipment().itemBuffer == targetItem) return OK;
            if(itemContainer.isItemInContainer(item)) return addPreAction(new GetItemFromContainerAction(item, itemContainer.contained.get(item).entity)); // take from container
            if(itemContainer.isItemOnMap(item)) return addPreAction(new GetItemFromGroundAction(item)); // pickup from ground
            if (itemContainer.isItemEquipped(item) && itemContainer.equipped.get(item).entity == task.performer)
                return addPreAction(new GetItemFromInventory(item)); // item in performers inventory
            return FAIL; // item is not registered in container or equipped on another unit
        };

        onStart = () -> maxProgress = 0;
    }
}
