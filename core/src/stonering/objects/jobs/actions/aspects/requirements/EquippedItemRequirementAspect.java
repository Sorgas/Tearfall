package stonering.objects.jobs.actions.aspects.requirements;

import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.aspects.effect.EquipItemEffectAspect;
import stonering.objects.jobs.actions.aspects.target.ItemTargetAspect;
import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.unit.aspects.EquipmentAspect;

/**
 * @author Alexander Kuzyakov on 27.01.2018.
 *
 * checks if unis has specific equipped item,
 * creates action to equip one if needed and possible
 */
public class EquippedItemRequirementAspect extends RequirementsAspect {
    private String requiredProperty;

    public EquippedItemRequirementAspect(Action action, String requiredProperty) {
        super(action);
        this.requiredProperty = requiredProperty;
    }

    @Override
    public boolean check() {
        Aspect aspect = action.getPerformer().getAspects().get("equipment");
        if (aspect != null) {
            if (((EquipmentAspect) aspect).getItemWithAspectAndProperty(requiredProperty) != null) {
                return true;
            } else {
                return addActionToTask();
            }
        }
        return false;
    }

    private boolean addActionToTask() {
        Item target = action.getGameContainer().getItemContainer().getItemWithProperty(requiredProperty);
        if (target != null) {
            Action newAction = new Action(action.getGameContainer());
            newAction.setEffectAspect(new EquipItemEffectAspect(newAction));
            newAction.setTargetAspect(new ItemTargetAspect(action, target));
            newAction.setRequirementsAspect(new BodyPartRequirementAspect(newAction, "grab"));
            newAction.setTask(action.getTask());
            action.getTask().addAction(newAction);
            return true;
        }
        return false;
    }
}
