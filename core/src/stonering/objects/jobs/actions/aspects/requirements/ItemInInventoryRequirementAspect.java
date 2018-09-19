package stonering.objects.jobs.actions.aspects.requirements;

import stonering.game.core.model.lists.ItemContainer;
import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.aspects.effect.PickUpItemEffectAspect;
import stonering.objects.jobs.actions.aspects.target.ItemTargetAspect;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.unit.aspects.EquipmentAspect;

import java.util.ArrayList;

/**
 * @author Alexander on 23.07.2018.
 */
public class ItemInInventoryRequirementAspect extends RequirementsAspect {
    private Item item;

    public ItemInInventoryRequirementAspect(Action action, Item item) {
        super(action);
        this.item = item;
    }

    @Override
    public boolean check() {
        ArrayList<Item> items = ((EquipmentAspect) action.getTask().getPerformer().getAspects().get("equipment")).getHauledItems();
        if (items.contains(item)) {
            return true;
        } else {
            return tryCreatePickingAction();
        }
    }

    private boolean tryCreatePickingAction() {
        System.out.println("creating picking action");
        Position target = action.getTargetPosition();
        ItemContainer itemContainer = action.getGameContainer().getItemContainer();
        if (itemContainer.isItemAvailableFrom(item, target)) {
            Action pickAction = new Action(action.getGameContainer());
            pickAction.setRequirementsAspect(new BodyPartRequirementAspect(pickAction, "grab"));
            pickAction.setTargetAspect(new ItemTargetAspect(pickAction, item));
            pickAction.setEffectAspect(new PickUpItemEffectAspect(pickAction));
            action.getTask().addFirstAction(pickAction);
            return true;
        }
        return false;
    }
}
