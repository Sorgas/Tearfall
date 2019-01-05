package stonering.entity.jobs.actions.aspects.requirements;

import stonering.entity.local.items.selectors.ItemSelector;
import stonering.game.core.model.lists.ItemContainer;
import stonering.global.utils.Position;
import stonering.entity.jobs.actions.Action;
import stonering.entity.jobs.actions.aspects.effect.PickUpItemEffectAspect;
import stonering.entity.jobs.actions.aspects.target.ItemTargetAspect;
import stonering.entity.local.items.Item;
import stonering.entity.local.unit.aspects.EquipmentAspect;

import java.util.ArrayList;

/**
 * @author Alexander on 23.07.2018.
 */
public class ItemInInventoryRequirementAspect extends RequirementsAspect {
    private ItemSelector itemSelector;

    public ItemInInventoryRequirementAspect(Action action, ItemSelector itemSelector) {
        super(action);
        this.itemSelector = itemSelector;
    }

    @Override
    public boolean check() {
        ArrayList<Item> items = ((EquipmentAspect) action.getTask().getPerformer().getAspects().get(EquipmentAspect.NAME)).getHauledItems();
        return itemSelector.check(items) || tryCreatePickingAction();
    }

    private boolean tryCreatePickingAction() {
        System.out.println("creating picking action");
        Position target = action.getTargetPosition();
        ItemContainer itemContainer = action.getGameContainer().getItemContainer();
        Item item = itemContainer.getItemAvailableBySelector(itemSelector, target);
        if (item != null) {
            Action pickAction = new Action(action.getGameContainer());
            pickAction.setRequirementsAspect(new BodyPartRequirementAspect(pickAction, "grab", true));
            pickAction.setTargetAspect(new ItemTargetAspect(pickAction, item));
            pickAction.setEffectAspect(new PickUpItemEffectAspect(pickAction));
            action.getTask().addFirstPreAction(pickAction);
            return true;
        }
        return false;
    }
}
