package stonering.entity.job.action.equipment;

import stonering.entity.job.action.target.ItemActionTarget;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.GrabEquipmentSlot;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.unit.CreatureEquipmentSystem;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.logging.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for picking and hauling item.
 * Performer should have {@link EquipmentAspect} and {@link GrabEquipmentSlot} free.
 * Item should be on the ground (see {@link ObtainItemAction}).
 *
 * @author Alexander on 12.01.2019.
 */
public class ItemPickupAction extends EquipmentAction {
    private Item item;

    public ItemPickupAction(Item item) {
        super(new ItemActionTarget(item));
        this.item = item;
        CreatureEquipmentSystem system = GameMvc.model().get(UnitContainer.class).equipmentSystem;
        LocalMap map = GameMvc.model().get(LocalMap.class);

        startCondition = () -> {
            EquipmentAspect equipment = task.performer.get(EquipmentAspect.class);
            if (!container.itemMap.get(item.position).contains(item)
                    || !map.passageMap.inSameArea(item.position, task.performer.position)) return FAIL; // item not available
            if (equipment().grabSlotStream().noneMatch(slot -> slot.grabbedItem == null)) // if no empty grab slots
                return addPreAction(new FreeGrabSlotAction()); // free another slot
            return OK;
        };

        onStart = () -> maxProgress = 20;

        onFinish = () -> { // add item to unit
            container.onMapItemsSystem.removeItemFromMap(item);
            equipment().items.add(item);
            equipment().grabSlotStream()
                    .filter(slot -> slot.grabbedItem == null)
                    .findFirst()
                    .ifPresent(slot -> system.fillGrabSlot(equipment(), slot, item));
            Logger.EQUIPMENT.logDebug("Item " + item + " has been picked up.");
        };
    }

    @Override
    public String toString() {
        return super.toString() + "pick up " + item.title;
    }
}
