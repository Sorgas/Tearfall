package stonering.entity.job.action.equipment;

import stonering.entity.item.Item;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.target.SelfActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.GrabEquipmentSlot;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.CreatureEquipmentSystem;
import stonering.game.model.system.unit.UnitContainer;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

/**
 * Makes creature to drop some item it holds in its grab slots.
 * Frees first found not free slot.
 * Is failed, if all slots are already free, as next action expects increasing number of free slots.
 *
 * @author Alexander on 09.02.2020
 */
public class FreeGrabSlotAction extends Action {

    protected FreeGrabSlotAction() {
        super(new SelfActionTarget());
        CreatureEquipmentSystem system = GameMvc.model().get(UnitContainer.class).equipmentSystem;
        ItemContainer container = GameMvc.model().get(ItemContainer.class);
        startCondition = () -> findSlot() == null ? FAIL : OK; // fail if unable to free any more slots
        onFinish = () -> {
            GrabEquipmentSlot slot = findSlot(); // should never be null
            Item item = system.freeGrabSlot(slot);
            container.onMapItemsSystem.putItem(item, task.performer.position); // put to map
        };
    }

    private GrabEquipmentSlot findSlot() {
        return task.performer.get(EquipmentAspect.class).grabSlots.values().stream()
                .filter(slot -> slot.grabbedItem != null)
                .findFirst().orElse(null);
    }
}
