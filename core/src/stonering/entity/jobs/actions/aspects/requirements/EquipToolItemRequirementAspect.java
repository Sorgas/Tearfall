package stonering.entity.jobs.actions.aspects.requirements;

import stonering.exceptions.NotSuitableItemException;
import stonering.entity.jobs.actions.Action;
import stonering.entity.jobs.actions.aspects.effect.UnequipItemEffectAspect;
import stonering.entity.jobs.actions.aspects.target.ItemTargetAspect;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ExactItemSelector;
import stonering.entity.local.unit.aspects.EquipmentAspect;

/**
 * Checks if tool item can be equipped by creature.
 * Creates action for moving items from hands to weared containers or unequipping them.
 *
 * @author Alexander on 23.09.2018.
 */
public class EquipToolItemRequirementAspect extends RequirementsAspect {
    private Item item;

    public EquipToolItemRequirementAspect(Action action, Item item) {
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
        Action action = new Action(this.action.getGameContainer());
        action.setTargetAspect(new ItemTargetAspect(action, item));
        //TODO count work amount based on item weight and creature stats
        action.setEffectAspect(new UnequipItemEffectAspect(action, 10));
        action.setRequirementsAspect(new EquippedItemRequirementAspect(action, new ExactItemSelector(item)));
        this.action.getTask().addFirstPreAction(action);
        //TODO create equip action
        return true;
    }
}
