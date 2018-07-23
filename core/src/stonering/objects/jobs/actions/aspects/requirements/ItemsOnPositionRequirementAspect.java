package stonering.objects.jobs.actions.aspects.requirements;

import stonering.game.core.model.lists.ItemContainer;
import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.aspects.effect.DropItemEffectAspect;
import stonering.objects.jobs.actions.aspects.target.BlockTargetAspect;
import stonering.objects.jobs.actions.aspects.target.ItemTargetAspect;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.items.ItemSelector;

import java.util.ArrayList;

/**
 * Checks if specific itemSelectors are on desired position.
 * Creates hauling actions if needed.
 *
 * @author Alexander on 08.07.2018.
 */
public class ItemsOnPositionRequirementAspect extends RequirementsAspect {
    private Position target;
    private ArrayList<ItemSelector> itemSelectors;

    public ItemsOnPositionRequirementAspect(Action action, Position target, ArrayList<ItemSelector> itemSelectors) {
        super(action);
        this.target = target;
        this.itemSelectors = itemSelectors;
    }

    @Override
    public boolean check() {
        System.out.println("checking items on position");
        ArrayList<Item> itemsOnSite = new ArrayList<>();
        ArrayList<Item> checkedItems = new ArrayList<>();
        itemsOnSite.addAll(action.getGameContainer().getItemContainer().getItems(target));
        for (ItemSelector itemSelector : itemSelectors) {
            ArrayList<Item> selectedItems = itemSelector.selectItems(itemsOnSite);
            if (selectedItems != null) {
                for (int i = 0; i < selectedItems.size(); i++) {
                    Item item = selectedItems.get(i);
                    if (!checkedItems.contains(item)) {
                        checkedItems.add(item);
                        itemsOnSite.remove(item);
                    } else {
                        // not valid branch
                        System.out.println(item.toString() + " already selected by another ItemSelector");
                    }
                }
            } else {
                return tryCreateDroppingAction(itemSelector);
            }
        }
        return true;
    }

    private boolean tryCreateDroppingAction(ItemSelector itemSelector) {
        ItemContainer itemContainer = action.getGameContainer().getItemContainer();
        if (itemContainer.hasItemsAvailableBySelector(itemSelector, target)) {
            Item item = itemContainer.getItemAvailableBySelector(itemSelector, target);
            if (item != null) {
                Action dropAction = new Action(action.getGameContainer());
                dropAction.setRequirementsAspect(new ItemInInventoryRequirementAspect(dropAction, item));
                dropAction.setTargetAspect(new ItemTargetAspect(dropAction, item));
                dropAction.setEffectAspect(new DropItemEffectAspect(dropAction, item));
                action.getTask().addAction(dropAction);
                return true;
            }
        }
        return false;
    }

    public ArrayList<ItemSelector> getItemSelectors() {
        return itemSelectors;
    }
}
