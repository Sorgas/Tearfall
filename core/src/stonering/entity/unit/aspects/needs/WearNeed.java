package stonering.entity.unit.aspects.needs;

import stonering.entity.job.action.EquipItemAction;
import stonering.entity.job.Task;
import stonering.entity.job.action.TaskTypesEnum;
import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.item.selectors.WearForSlotItemSelector;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.enums.TaskPrioritiesEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.items.ItemContainer;

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
    public TaskPrioritiesEnum countPriority(Entity entity) {
        EquipmentAspect equipmentAspect = entity.getAspect(EquipmentAspect.class);
        if (equipmentAspect != null) {
            if (!equipmentAspect.getEmptyDesiredSlots().isEmpty()) {
                return TaskPrioritiesEnum.HEALTH_NEEDS;
            }

        }
        return TaskPrioritiesEnum.NONE;
    }

    /**
     * Creates task to equip wear if needed.
     */
    @Override
    public Task tryCreateTask(Entity entity) {
        EquipmentAspect equipmentAspect = entity.getAspect(EquipmentAspect.class);
        if (equipmentAspect == null) return null;
        if (equipmentAspect.getEmptyDesiredSlots().isEmpty()) return null;
        for (EquipmentSlot equipmentSlot : equipmentAspect.desiredSlots) {
            Task task = tryCreateEquipTask(entity, equipmentSlot);
            if (task != null) return task;
        }
        return null;
    }

    /**
     * Attempts to create task for equipping wear into given slot.
     * Task is not created, if item cannot be found on map.
     */
    private Task tryCreateEquipTask(Entity entity, EquipmentSlot equipmentSlot) {
        ItemSelector itemSelector = new WearForSlotItemSelector(equipmentSlot.name);
        Item item = GameMvc.instance().getModel().get(ItemContainer.class).util.getItemAvailableBySelector(itemSelector, entity.position);
        if (item == null) return null;
        EquipItemAction equipItemAction = new EquipItemAction(item, true);
        return new Task("Equip item " + item.getTitle(), equipItemAction, GET_WEAR_PRIORITY);
    }
}
