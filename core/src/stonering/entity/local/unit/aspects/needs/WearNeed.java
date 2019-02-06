package stonering.entity.local.unit.aspects.needs;

import stonering.entity.jobs.actions.EquipItemAction;
import stonering.game.core.model.GameModel;
import stonering.entity.jobs.Task;
import stonering.entity.jobs.actions.TaskTypesEnum;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.items.selectors.WearForLimbItemSelector;
import stonering.entity.local.unit.aspects.EquipmentAspect;
import stonering.game.core.model.lists.ItemContainer;

/**
 * Basic need for clothes. Each creature species has own list of limbs to be covered by wear.
 *
 * @author Alexander Kuzyakov on 21.09.2018.
 */
public class WearNeed extends Need {
    private static final int GET_WEAR_PRIORITY = 4;

    /**
     * Counts current priority for creature to find wear.
     * Having no wear only gives comfort penalty, so priority is never high.
     *
     * @return
     */
    @Override
    public int countPriority() {
        EquipmentAspect equipmentAspect = (EquipmentAspect) aspectHolder.getAspects().get(EquipmentAspect.NAME);
        if (equipmentAspect != null) {
            if (!equipmentAspect.getEmptyDesiredSlots().isEmpty()) {
                return GET_WEAR_PRIORITY;
            }
        }
        return -1;
    }

    @Override
    public Task tryCreateTask() {
        EquipmentAspect equipmentAspect = (EquipmentAspect) aspectHolder.getAspects().get("equipment");
        if (equipmentAspect != null) {
            if (!equipmentAspect.getEmptyDesiredSlots().isEmpty()) {
                for (EquipmentAspect.EquipmentSlot equipmentSlot : equipmentAspect.getDesiredSlots()) {
                    if (equipmentSlot.isEmpty()) {
                        Task task = tryCreateEquipTask(aspectHolder, container, equipmentSlot);
                        if (task != null) {
                            return task;
                        }
                    }
                }
            }
        }
        return null;
    }

    private Task tryCreateEquipTask(AspectHolder aspectHolder, GameModel container, EquipmentAspect.EquipmentSlot equipmentSlot) {
        ItemSelector itemSelector = createItemSelectorForLimb(equipmentSlot.limbName);
        Item item = container.get(ItemContainer.class).getItemAvailableBySelector(itemSelector, aspectHolder.getPosition());
        Task task = null;
        if (item != null) {
            EquipItemAction equipItemAction = new EquipItemAction(item, true);
            task = new Task("Equip item " + item.getTitle(), TaskTypesEnum.EQUIPPING, equipItemAction, GET_WEAR_PRIORITY);
        }
        return task;
    }

    private ItemSelector createItemSelectorForLimb(String limbName) {
        return new WearForLimbItemSelector(limbName);
    }
}
