package stonering.entity.job.action.equipment;

import stonering.entity.item.Item;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.target.SelfActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

/**
 * Fictive action to handle different cases of item placement.
 * Checks item placement and creates corresponding pre-action. (pick up from ground, get from container).
 *
 * @author Alexander on 04.02.2020.
 */
public class ObtainItemAction extends Action {

    public ObtainItemAction(Item item) {
        super(new SelfActionTarget());
        startCondition = () -> {
            ItemContainer itemContainer = GameMvc.model().get(ItemContainer.class); // item is in container
            if(task.performer.get(EquipmentAspect.class).hauledItems.contains(item)) return OK;
            if (itemContainer.equippedItemsSystem.isItemEquipped(item)) return FAIL; // item is equipped on another unit
            if (itemContainer.containedItemsSystem.isItemContained(item))
                return addPreAction(new GetItemFromContainerAction(item, itemContainer.contained.get(item).entity)); // take from container
            return addPreAction(new ItemPickupAction(item)); // pickup from ground
        };
    }
}
