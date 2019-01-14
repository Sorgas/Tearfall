package stonering.entity.jobs.actions.aspects.requirements;

import stonering.entity.jobs.actions.Action;
import stonering.entity.jobs.actions.aspects.effect.EquipItemEffectAspect;
import stonering.entity.jobs.actions.aspects.target.ItemActionTarget;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.unit.aspects.EquipmentAspect;

/**
 * Checks if unis has specific equipped item,
 * creates action to equip one if needed and possible.
 *
 * @author Alexander Kuzyakov on 27.01.2018.
 */
public class EquippedItemRequirementAspect extends RequirementsAspect {
    ItemSelector itemSelector;

    public EquippedItemRequirementAspect(Action action, ItemSelector itemSelector) {
        super(action);
        this.itemSelector = itemSelector;
    }

    @Override
    public boolean check() {
        EquipmentAspect aspect = (EquipmentAspect) action.getTask().getPerformer().getAspects().get("equipment");
        if (aspect != null) {
            return itemSelector.check(aspect.getEquippedItems()) || addActionToTask();
        }
        return false;
    }

    private boolean addActionToTask() {
        Item target = action.getGameContainer().getItemContainer().getItemAvailableBySelector(itemSelector, action.getTask().getPerformer().getPosition());
        if (target != null) {
            Action newAction = new Action(action.getGameContainer());
            newAction.setEffectAspect(new EquipItemEffectAspect(newAction));
            newAction.setTargetAspect(new ItemActionTarget(action, target));
            if (target.isWear()) {
                newAction.setRequirementsAspect(new EquipWearItemRequirementAspect(newAction, target));
            } else if (target.isTool()) {
                newAction.setRequirementsAspect(new EquipToolItemRequirementAspect(newAction, target));
            } else {
                System.out.println("non-tool or wear item selected for equipping.");
                return false;
            }
            action.getTask().addFirstPreAction(newAction);
            return true;
        }
        return false;
    }
}
