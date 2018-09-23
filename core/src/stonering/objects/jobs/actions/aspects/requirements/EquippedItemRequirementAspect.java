package stonering.objects.jobs.actions.aspects.requirements;

import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.aspects.effect.EquipItemEffectAspect;
import stonering.objects.jobs.actions.aspects.target.ItemTargetAspect;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.items.selectors.ItemSelector;
import stonering.objects.local_actors.unit.aspects.EquipmentAspect;

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
            return itemSelector.check(aspect.getItems()) || addActionToTask();
        }
        return false;
    }

    private boolean addActionToTask() {
        Item target = action.getGameContainer().getItemContainer().getItemAvailableBySelector(itemSelector, action.getTask().getPerformer().getPosition());
        if (target != null) {
            Action newAction = new Action(action.getGameContainer());
            newAction.setEffectAspect(new EquipItemEffectAspect(newAction));
            newAction.setTargetAspect(new ItemTargetAspect(action, target));
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
