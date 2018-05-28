package stonering.objects.jobs.actions.aspects.requirements;

import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.ActionTypeEnum;
import stonering.objects.jobs.actions.aspects.effect.EquipItemEffectAspect;
import stonering.objects.jobs.actions.aspects.target.ItemTargetAspect;
import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.unit.aspects.EquipmentAspect;

/**
 * Created by Alexander on 27.01.2018.
 *
 * checks if unis has specific equipped item,
 * creates action to equip one if needed and possible
 */
public class EquippedItemRequirementAspect extends RequirementsAspect {
    private String itemAspect;
    private String requiredReaction;

    public EquippedItemRequirementAspect(Action action, String itemAspect, String requiredReaction) {
        super(action);
        this.itemAspect = itemAspect;
        this.requiredReaction = requiredReaction;
    }

    @Override
    public boolean check() {
        Aspect aspect = action.getPerformer().getAspects().get("equipment");
        if (aspect != null) {
            if (((EquipmentAspect) aspect).getItemWithAspectAndProperty(itemAspect, requiredReaction) != null) {
                return true;
            } else {
                return addActionToTask();
            }
        }
        return false;
    }

    private boolean addActionToTask() {
        Item target = action.getGameContainer().getItemContainer().getItemWithAspect(itemAspect, requiredReaction);
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
