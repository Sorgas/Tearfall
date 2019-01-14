package stonering.entity.jobs.actions.aspects.requirements;

import stonering.entity.jobs.actions.aspects.requirements.ComplexRequirementAspect.FunctionsEnum;
import stonering.entity.jobs.actions.aspects.target.ItemActionTarget;
import stonering.exceptions.NotSuitableItemException;
import stonering.entity.jobs.actions.Action;
import stonering.entity.jobs.actions.aspects.effect.EquipItemEffectAspect;
import stonering.entity.jobs.actions.aspects.effect.UnequipItemEffectAspect;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ExactItemSelector;
import stonering.entity.local.unit.aspects.EquipmentAspect;

/**
 * Checks if task performer can equip specified wear item.
 * Creates actions to put off/on items occupying higher layers in slots required by item and layer of this item.
 * Fails action if there is no such slots on this creature.
 *
 * @author Alexander on 22.09.2018.
 */
public class EquipWearItemRequirementAspect extends RequirementsAspect {
    private Item item;

    public EquipWearItemRequirementAspect(Action action, Item item) {
        super(action);
        this.item = item;
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
        action.setRequirementsAspect(new ComplexRequirementAspect(action, requirements, FunctionsEnum.AND));
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
        action.setRequirementsAspect(new ComplexRequirementAspect(action, requirementsAspects, FunctionsEnum.AND));
        action.setRequirementsAspect(new ComplexRequirementAspect(action, requirements, FunctionsEnum.AND));
        this.action.getTask().addFirstPreAction(action);
        return true;
    }
}
