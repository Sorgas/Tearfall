package stonering.entity.jobs.actions.aspects.requirements;

import stonering.entity.jobs.actions.Action;
import stonering.entity.jobs.actions.aspects.effect.UnequipItemEffectAspect;
import stonering.entity.jobs.actions.aspects.requirements.ComplexRequirementAspect.FunctionsEnum;
import stonering.entity.jobs.actions.aspects.target.ItemTargetAspect;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ExactItemSelector;
import stonering.entity.local.items.selectors.SingleItemSelector;
import stonering.entity.local.unit.aspects.EquipmentAspect;

/**
 * Checks if task performer can unequip specified item.
 * Creates actions to put off/on items, occupying slot layers above this item.
 * Fails action if there is no item equipped.
 *
 * @author Alexander on 22.09.2018.
 */
public class UnequipItemRequirementAspect extends RequirementsAspect {
    private SingleItemSelector itemSelector;

    public UnequipItemRequirementAspect(Action action, SingleItemSelector itemSelector) {
        super(action);
        this.itemSelector = itemSelector;
    }

    @Override
    public boolean check() {
        EquipmentAspect equipmentAspect = ((EquipmentAspect) action.getTask().getPerformer().getAspects().get("equipment"));
        if (equipmentAspect != null) {
            Item item = itemSelector.selectItem(equipmentAspect.getEquippedItems());
            if (item != null) {
                for (EquipmentAspect.EquipmentSlot slot : equipmentAspect.getSlots().values()) {
                    if (slot.items.contains(item)) {
                        for (int i = slot.items.size() - 1; i >= 0; i--) {
                            if (slot.items.get(i).getType().getWear().getLayer() > item.getType().getWear().getLayer()) {
                                return tryAddUnequipAction(slot.items.get(i));
                            }
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean tryAddUnequipAction(Item item) {
        Action action = new Action(this.action.getGameContainer());
        action.setTargetAspect(new ItemTargetAspect(action, item));
        //TODO count work amount based on item weight and creature stats
        action.setEffectAspect(new UnequipItemEffectAspect(action, 10));
        RequirementsAspect[] requirements = {
                new EquippedItemRequirementAspect(action, new ExactItemSelector(item)),
                new UnequipItemRequirementAspect(action, new ExactItemSelector(item))
        };
        action.setRequirementsAspect(new ComplexRequirementAspect(action, requirements, FunctionsEnum.AND));
        return true;
    }
}
