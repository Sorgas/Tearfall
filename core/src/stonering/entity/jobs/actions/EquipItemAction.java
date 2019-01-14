package stonering.entity.jobs.actions;

import stonering.entity.jobs.actions.aspects.effect.EquipItemEffectAspect;
import stonering.entity.jobs.actions.aspects.effect.UnequipItemEffectAspect;
import stonering.entity.jobs.actions.aspects.requirements.*;
import stonering.entity.jobs.actions.aspects.target.ItemActionTarget;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ExactItemSelector;
import stonering.entity.local.unit.aspects.EquipmentAspect;
import stonering.exceptions.NotSuitableItemException;

public class EquipItemAction extends Action {
    private Item item;

    public EquipItemAction(Item item) {
        this.item = item;
    }

    @Override
    public boolean perform() {
        if (((EquipmentAspect) task.getPerformer().getAspects().get("equipment")).equipItem(item)) {
            //TODO manage equipped items in item container
            gameMvc.getModel().getItemContainer().pickItem(item);
            return true;
        }
        return false;
    }

    @Override
    public boolean check() {
        EquipmentAspect equipmentAspect = (EquipmentAspect) action.getTask().getPerformer().getAspects().get("equipment");
        try {
            Item item = equipmentAspect.checkItemForEquip(this.item);
            if (item != null) {
                return createUnequipAction(item);
            }
            return true;
        } catch (NotSuitableItemException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean createUnequipAction(Item item) {
        if (!item.isTool() && !item.isWear()) {
            return false;
        }
        // unequip action
        Action action = new Action(this.action.getGameContainer());
        action.setTargetAspect(new ItemActionTarget(action, item));
        //TODO count work amount based on item weight and creature stats
        action.setEffectAspect(new UnequipItemEffectAspect(action, 10));
        RequirementsAspect[] requirements = {
                new EquippedItemRequirementAspect(action, new ExactItemSelector(item)),
                new UnequipItemRequirementAspect(action, new ExactItemSelector(item))
        };
        action.setRequirementsAspect(new ComplexRequirementAspect(action, requirements, ComplexRequirementAspect.FunctionsEnum.AND));
        this.action.getTask().addFirstPreAction(action);

        // equip action
        action = new Action(this.action.getGameContainer());
        action.setTargetAspect(new ItemActionTarget(action, item));
        action.setEffectAspect(new EquipItemEffectAspect(action));
        RequirementsAspect[] requirementsAspects;
        if (item.isWear() || item.isTool()) {
            requirementsAspects = new RequirementsAspect[]{
                    new BodyPartRequirementAspect(action, "grab", true),
                    item.isWear() ? new EquipWearItemRequirementAspect(action, item) : new EquipToolItemRequirementAspect(action, item)};
        } else {
            return false;
        }
        action.setRequirementsAspect(new ComplexRequirementAspect(action, requirementsAspects, ComplexRequirementAspect.FunctionsEnum.AND));
        action.setRequirementsAspect(new ComplexRequirementAspect(action, requirements, ComplexRequirementAspect.FunctionsEnum.AND));
        this.action.getTask().addFirstPreAction(action);
        return true;
    }

    @Override
    public boolean check() {
        EquipmentAspect equipmentAspect = (EquipmentAspect) action.getTask().getPerformer().getAspects().get("equipment");
        try {
            Item item = equipmentAspect.checkItemForEquip(this.item);
            if (item != null) {
                return createUnequipAction(item);
            }
            return true;
        } catch (NotSuitableItemException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean createUnequipAction(Item item) {
        Action action = new Action(this.action.getGameContainer());
        action.setTargetAspect(new ItemActionTarget(action, item));
        //TODO count work amount based on item weight and creature stats
        action.setEffectAspect(new UnequipItemEffectAspect(action, 10));
        action.setRequirementsAspect(new EquippedItemRequirementAspect(action, new ExactItemSelector(item)));
        this.action.getTask().addFirstPreAction(action);
        //TODO create equip action
        return true;
    }
}
