package stonering.entity.job.action;

import stonering.entity.job.action.target.ItemActionTarget;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.util.global.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

/**
 * Action for picking and hauling item. Performer should have {@link EquipmentAspect}.
 * Item should be on the ground.
 *
 * @author Alexander on 12.01.2019.
 */
public class ItemPickupAction extends Action {

    public ItemPickupAction(Item item) {
        super(new ItemActionTarget(item));

        startCondition = () -> { // unit is able to carry items, item persists
            Logger.TASKS.logDebug("Checking picking action");
            if (task.performer.getAspect(EquipmentAspect.class) == null) return FAIL; // performer cannot pick up item
            if (GameMvc.model().get(ItemContainer.class).itemMap.get(item.position).contains(item)) return OK;
            return FAIL;
        };

        onFinish = () -> { // add item to unit
            EquipmentAspect equipment = task.performer.getAspect(EquipmentAspect.class);
            ItemContainer container = GameMvc.model().get(ItemContainer.class);

            container.onMapItemsSystem.removeItemFromMap(item);
            equipment.pickupItem(item);
            container.equippedItemsSystem.itemEquipped(item, equipment);
        };
    }
}
