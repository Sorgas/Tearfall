package stonering.entity.job.action;

import stonering.entity.job.action.target.ItemActionTarget;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.GrabEquipmentSlot;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.CreatureEquipmentSystem;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.global.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for picking and hauling item.
 * Performer should have {@link EquipmentAspect} and {@link GrabEquipmentSlot} free.
 * Item should be on the ground (see {@link ObtainItemAction}).
 *
 * @author Alexander on 12.01.2019.
 */
public class ItemPickupAction extends Action {

    public ItemPickupAction(Item item) {
        super(new ItemActionTarget(item));
        CreatureEquipmentSystem system = GameMvc.model().get(UnitContainer.class).equipmentSystem;
        ItemContainer itemContainer = GameMvc.model().get(ItemContainer.class);
        LocalMap map = GameMvc.model().get(LocalMap.class);

        startCondition = () -> {
            EquipmentAspect equipment = task.performer.getAspect(EquipmentAspect.class);
            Logger.TASKS.logDebug("Checking picking action");
            if (!itemContainer.itemMap.get(item.position).contains(item) || !map.passageMap.inSameArea(item.position, task.performer.position))
                return FAIL; // item not available
            if (!system.canPickUpItem(equipment, item)) {
                task.addFirstPreAction(new FreeGrabSlotAction()); // free another slot
                return NEW;
            }
            return OK;
        };

        onFinish = () -> { // add item to unit
            system.pickUpItem(task.performer.getAspect(EquipmentAspect.class), item);
        };
    }
}
