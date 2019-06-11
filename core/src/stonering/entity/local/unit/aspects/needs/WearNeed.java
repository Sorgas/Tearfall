package stonering.entity.local.unit.aspects.needs;

import stonering.entity.job.action.EquipItemAction;
import stonering.entity.job.Task;
import stonering.entity.job.action.TaskTypesEnum;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.items.selectors.WearForLimbItemSelector;
import stonering.entity.local.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.local.unit.aspects.equipment.EquipmentSlot;
import stonering.game.model.lists.ItemContainer;

/**
 * Basic need for clothes. Each creature species has own list of limbs to be covered by wear.
 *
 * @author Alexander Kuzyakov on 21.09.2018.
 */
public class WearNeed extends Need {
    private static final int GET_WEAR_PRIORITY = 4; //priority for equipping items into desired slots.

    /**
     * Counts current priority for creature to find wear.
     * Having no wear only gives comfort penalty, so priority is never high.
     * TODO add prioritizing based on environment temperature
     * @param aspectHolder
     */
    @Override
    public int countPriority(AspectHolder aspectHolder) {
        EquipmentAspect equipmentAspect = (EquipmentAspect) aspectHolder.getAspect(EquipmentAspect.class);
        if (equipmentAspect != null) {
            if (!equipmentAspect.getEmptyDesiredSlots().isEmpty()) {
                return GET_WEAR_PRIORITY;
            }
        }
        return -1;
    }

    @Override
    public Task tryCreateTask(AspectHolder aspectHolder) {
        EquipmentAspect equipmentAspect = aspectHolder.getAspect(EquipmentAspect.class);
        if (equipmentAspect == null) return null;
        if (equipmentAspect.getEmptyDesiredSlots().isEmpty()) return null;
        for (EquipmentSlot equipmentSlot : equipmentAspect.getDesiredSlots()) {
            Task task = tryCreateEquipTask(aspectHolder, equipmentSlot);
            if (task != null) return task;
        }
        return null;
    }

    private Task tryCreateEquipTask(AspectHolder aspectHolder, EquipmentSlot equipmentSlot) {
        ItemSelector itemSelector = createItemSelectorForLimb(equipmentSlot.limbName);
        Item item = container.get(ItemContainer.class).getItemAvailableBySelector(itemSelector, aspectHolder.getPosition());
        if (item == null) return null;
        EquipItemAction equipItemAction = new EquipItemAction(item, true);
        return new Task("Equip item " + item.getTitle(), TaskTypesEnum.EQUIPPING, equipItemAction, GET_WEAR_PRIORITY);
    }

    private ItemSelector createItemSelectorForLimb(String limbName) {
        return new WearForLimbItemSelector(limbName);
    }
}
