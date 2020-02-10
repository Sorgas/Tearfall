package stonering.entity.job.action.equipment;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.target.SelfActionTarget;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.NEW;

/**
 * Fictive action to handle different cases of item placement.
 * Checks item placement an creates corresponding pre-action. (pick up from ground, get from container).
 * Never fails itself and is auto finished.
 *
 * @author Alexander on 04.02.2020.
 */
public class ObtainItemAction extends Action {

    public ObtainItemAction(Item item) {
        super(new SelfActionTarget());
        startCondition = () -> {
            ItemContainer itemContainer = GameMvc.model().get(ItemContainer.class); // item is in container
            if (itemContainer.containedItemsSystem.isItemContained(item)) {
                Entity container = itemContainer.contained.get(item).entity;
                task.addFirstPreAction(new GetItemFromContainerAction(item, container)); // take from container
            } else if (itemContainer.equippedItemsSystem.isItemEquipped(item)) { // item is equipped on another unit
                return FAIL;
                //TODO
            } else { // pickup from ground
                task.addFirstPreAction(new ItemPickupAction(item));
            }
            return NEW;
        };
    }
}
