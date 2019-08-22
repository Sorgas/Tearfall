package stonering.entity.unit.aspects.needs;

import stonering.entity.job.action.EquipItemAction;
import stonering.entity.job.Task;
import stonering.entity.job.action.TaskTypesEnum;
import stonering.entity.Entity;
import stonering.entity.PositionAspect;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.item.selectors.WearForLimbItemSelector;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.game.model.lists.ItemContainer;

/**
 * Basic need for clothes. Each creature species has own list of limbs to be covered by wear.
 *
 * @author Alexander Kuzyakov on 21.09.2018.
 */
public class WearNeed extends Need {
    private static final int GET_WEAR_PRIORITY = 4; //priority for equipping item into desired slots.

    /**
     * Counts current priority for creature to find wear.
     * Having no wear only gives comfort penalty, so priority is never high.
     * TODO add prioritizing based on environment temperature
     */
    @Override
    public int countPriority(Entity entity) {
        EquipmentAspect equipmentAspect = entity.getAspect(EquipmentAspect.class);
        if (equipmentAspect != null) {
            if (!equipmentAspect.getEmptyDesiredSlots().isEmpty()) {
                return GET_WEAR_PRIORITY;
            }
        }
        return -1;
    }

    /**
     * Creates task to equip wear if needed.
     */
    @Override
    public Task tryCreateTask(Entity entity) {
        EquipmentAspect equipmentAspect = entity.getAspect(EquipmentAspect.class);
        if (equipmentAspect == null) return null;
        if (equipmentAspect.getEmptyDesiredSlots().isEmpty()) return null;
        for (EquipmentSlot equipmentSlot : equipmentAspect.getDesiredSlots()) {
            Task task = tryCreateEquipTask(entity, equipmentSlot);
            if (task != null) return task;
        }
        return null;
    }

    private Task tryCreateEquipTask(Entity entity, EquipmentSlot equipmentSlot) {
        ItemSelector itemSelector = new WearForLimbItemSelector(equipmentSlot.limbName);
        Item item = container.get(ItemContainer.class).getItemAvailableBySelector(itemSelector, entity.getAspect(PositionAspect.class).position);
        if (item == null) return null;
        EquipItemAction equipItemAction = new EquipItemAction(item, true);
        return new Task("Equip item " + item.getTitle(), TaskTypesEnum.EQUIPPING, equipItemAction, GET_WEAR_PRIORITY);
    }
}
