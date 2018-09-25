package stonering.objects.local_actors.unit.aspects.needs;

import stonering.game.core.model.GameContainer;
import stonering.objects.jobs.Task;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.TaskTypesEnum;
import stonering.objects.jobs.actions.aspects.effect.EquipItemEffectAspect;
import stonering.objects.jobs.actions.aspects.requirements.BodyPartRequirementAspect;
import stonering.objects.jobs.actions.aspects.requirements.ComplexRequirementAspect;
import stonering.objects.jobs.actions.aspects.requirements.EquipWearItemRequirementAspect;
import stonering.objects.jobs.actions.aspects.requirements.RequirementsAspect;
import stonering.objects.jobs.actions.aspects.target.ItemTargetAspect;
import stonering.objects.local_actors.AspectHolder;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.items.selectors.ItemSelector;
import stonering.objects.local_actors.items.selectors.WearForLimbItemSelector;
import stonering.objects.local_actors.unit.aspects.EquipmentAspect;

/**
 * Basic need for clothes. Each creature species has own list of limbs to be covered by wear.
 *
 * @author Alexander on 21.09.2018.
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
        EquipmentAspect equipmentAspect = (EquipmentAspect) aspectHolder.getAspects().get("equipment");
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

    private Task tryCreateEquipTask(AspectHolder aspectHolder, GameContainer container, EquipmentAspect.EquipmentSlot equipmentSlot) {
        ItemSelector itemSelector = createItemSelectorForLimb(equipmentSlot.limbName);
        Item item = container.getItemContainer().getItemAvailableBySelector(itemSelector, aspectHolder.getPosition());
        Task task = null;
        if (item != null) {
            Action action = new Action(container);
            action.setTargetAspect(new ItemTargetAspect(action, item));
            RequirementsAspect[] requirementsAspects = {new BodyPartRequirementAspect(action, "grab", true),
                    new EquipWearItemRequirementAspect(action, item)};
            action.setRequirementsAspect(new ComplexRequirementAspect(action, requirementsAspects));
            action.setEffectAspect(new EquipItemEffectAspect(action));
            task = new Task("Equip item " + item.getTitle(), TaskTypesEnum.EQUIPPING, action, GET_WEAR_PRIORITY, container);
        }
        return task;
    }

    private ItemSelector createItemSelectorForLimb(String limbName) {
        return new WearForLimbItemSelector(limbName);
    }
}
