package stonering.entity.job.action.equipment;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.WearAspect;
import stonering.entity.job.action.target.SelfActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.game.GameMvc;
import stonering.game.model.system.unit.CreatureEquipmentSystem;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.logging.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

import java.util.Optional;

/**
 * Action for equipping wear items.
 *
 * @author Alexander
 */
public class EquipWearItemAction extends EquipmentAction {
    public Item item;

    public EquipWearItemAction(Item item) {
        super(new SelfActionTarget());
        this.item = item;
        CreatureEquipmentSystem system = GameMvc.model().get(UnitContainer.class).equipmentSystem;
        WearAspect wear = item.get(WearAspect.class);

        startCondition = () -> {
            if (!validate()) return FAIL;
            if (equipment().grabSlotStream().noneMatch(slot -> slot.grabbedItem == item)) { // item not in hands
                return addPreAction(new ObtainItemAction(item)); // get item
            }
            return OK;
        };

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
        WearAspect wear = item.get(WearAspect.class);
        if (wear == null) return Logger.TASKS.logError("Target item is not wear", false);
        if (equipment().slots.get(wear.slot) == null)
            return Logger.TASKS.logError("unit " + task.performer + " has no appropriate slots for item " + item, false);
        return true;
    }

    @Override
    public String toString() {
        return "Equipping wear item " + item.title + " action: ";
    }
}
