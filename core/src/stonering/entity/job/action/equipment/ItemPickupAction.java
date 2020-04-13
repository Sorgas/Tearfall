package stonering.entity.job.action.equipment;

import stonering.entity.job.action.Action;
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
    private Item item;

    public ItemPickupAction(Item item) {
        super(new ItemActionTarget(item));
        this.item = item;
        CreatureEquipmentSystem system = GameMvc.model().get(UnitContainer.class).equipmentSystem;
        ItemContainer itemContainer = GameMvc.model().get(ItemContainer.class);
        LocalMap map = GameMvc.model().get(LocalMap.class);

        startCondition = () -> {
            EquipmentAspect equipment = task.performer.get(EquipmentAspect.class);
            if (!itemContainer.itemMap.get(item.position).contains(item)
                    || !map.passageMap.inSameArea(item.position, task.performer.position)) return FAIL; // item not available
            if (!system.canPickUpItem(equipment, item)) { // if no empty grab slots
                task.addFirstPreAction(new FreeGrabSlotAction()); // free another slot
                return NEW;
            }
            return OK;
        };

        onFinish = () -> { // add item to unit
            EquipmentAspect equipment = task.performer.get(EquipmentAspect.class);
            itemContainer.onMapItemsSystem.removeItemFromMap(item);
            equipment.hauledItems.contains(item);
            itemContainer.equippedItemsSystem.itemEquipped(item, equipment);
            //            GrabEquipmentSlot slot = system.getSlotForPickingUpItem(equipment, item);
//            if (slot != null) {
//                system.fillGrabSlot(equipment, slot, item);
//                return;
//            }
            Logger.EQUIPMENT.logError("Slot for picking up item " + item + " not found");
        };
    }

    @Override
    public String toString() {
        return super.toString() + "pick up " + item;
    }
}
