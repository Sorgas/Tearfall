package stonering.objects.jobs.actions.aspects.requirements;

import stonering.game.core.model.lists.ItemContainer;
import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.aspects.effect.DropItemEffectAspect;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.unit.aspects.EquipmentAspect;

import java.util.ArrayList;

/**
 * @author Alexander on 23.07.2018.
 */
public class ItemInInventoryRequirementAspect extends RequirementsAspect {
    private Item itemSelector;

    public ItemInInventoryRequirementAspect(Action action, Item itemSelector) {
        super(action);
        this.itemSelector = itemSelector;
    }

    @Override
    public boolean check() {
        ArrayList<Item> items = ((EquipmentAspect) action.getPerformer().getAspects().get("equipment")).getInventory();
        if(itemSelector.check(items)) {
            return true;
        } else {

        }
    }

    private boolean tryCreatePickingAction() {
        Position target = action.getTargetPosition();
        ItemContainer itemContainer = action.getGameContainer().getItemContainer();
        if (itemContainer.hasItemsAvailableBySelector(itemSelector, target)) {
            Item item = itemContainer.getItemAvailableBySelector(itemSelector, target);
            if (item != null) {
                Action dropAction = new Action(action.getGameContainer());
                dropAction.setRequirementsAspect(new BodyPartRequirementAspect(dropAction, "grab"));
                dropAction.setTargetAspect(new Ite(dropAction, target));
                dropAction.setEffectAspect(new DropItemEffectAspect(dropAction, item));
                action.getTask().addAction(dropAction);
                return true;
            }
        }
        return false;
    }
}
