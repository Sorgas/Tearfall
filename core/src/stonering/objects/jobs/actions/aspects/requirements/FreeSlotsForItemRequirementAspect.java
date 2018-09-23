package stonering.objects.jobs.actions.aspects.requirements;

import stonering.exceptions.NotSuitableItemException;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.aspects.effect.UnequipItemEffactAspect;
import stonering.objects.jobs.actions.aspects.target.ItemTargetAspect;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.items.selectors.ExactItemSelector;
import stonering.objects.local_actors.unit.aspects.EquipmentAspect;

import java.util.List;

/**
 * Checks if task performer can equip specified item.
 * Creates actions to put off/on items occupying higher layers in slots required by item and layer of this item.
 * Fails action if there is no such slots on this creature.
 *
 * @author Alexander on 22.09.2018.
 */
public class FreeSlotsForItemRequirementAspect extends RequirementsAspect {
    private Item item;

    public FreeSlotsForItemRequirementAspect(Action action, Item item) {
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
        action.setEffectAspect(new UnequipItemEffactAspect(action, 10));
        RequirementsAspect[] requirements = {
                new EquippedItemRequirementAspect(action, new ExactItemSelector(item)),
                new EquippedItemOnTopLayerRequirementAspect(action, new ExactItemSelector(item))
        };
        action.setRequirementsAspect(new ComplexRequirementAspect(action, requirements));
        this.action.getTask().addFirstPreAction(action);
        //TODO create equip action
        return true;
    }
}
