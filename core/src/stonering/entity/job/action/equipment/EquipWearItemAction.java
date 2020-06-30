package stonering.entity.job.action.equipment;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.WearAspect;
import stonering.entity.job.action.target.SelfActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.game.GameMvc;
import stonering.game.model.system.unit.CreatureEquipmentSystem;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.logging.Logger;

import java.util.Optional;

/**
 * Action for equipping wear items.
 *
 * @author Alexander
 */
public class EquipWearItemAction extends PutItemToDestinationAction {

    public EquipWearItemAction(Item item) {
        super(new SelfActionTarget(), item);
        CreatureEquipmentSystem system = GameMvc.model().get(UnitContainer.class).equipmentSystem;
        WearAspect wear = item.get(WearAspect.class);

        onFinish = () -> {
            EquipmentSlot targetSlot = equipment().slots.get(wear.slot);
            // TODO wrap with layer condition
            Optional.ofNullable(targetSlot)
                    .map(system::freeSlot) // remove item from slot
                    .ifPresent(previousItem -> itemContainer.onMapItemsSystem.addItemToMap(previousItem, task.performer.position)); // put to map

            // move item from hand to slot
            equipment().grabSlotStream()
                    .filter(slot -> slot.grabbedItem == item)
                    .findFirst() // find slot with item
                    .ifPresent(slot -> {
                        system.freeGrabSlot(slot); // free hand
                        system.fillSlot(equipment(), targetSlot, item); // put on wear
                    });
        };
    }

    protected boolean validate() {
        if (!super.validate()) return false;
        WearAspect wear = targetItem.get(WearAspect.class);
        if (wear == null) return Logger.TASKS.logError("Target item is not wear", false);
        if (equipment().slots.get(wear.slot) == null)
            return Logger.TASKS.logError("unit " + task.performer + " has no appropriate slots for item " + targetItem, false);
        return true;
    }

    @Override
    public String toString() {
        return "Equipping wear item " + targetItem.title + " action: ";
    }
}
