package stonering.objects.jobs.actions.aspects.requirements;

import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.aspects.effect.DropItemEffectAspect;
import stonering.objects.jobs.actions.aspects.target.BlockTargetAspect;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.items.ItemSelector;

import java.util.ArrayList;

/**
 * Checks if specific items are on desired position.
 * Creates hauling actions if needed.
 *
 * @author Alexander on 08.07.2018.
 */
public class ItemsOnPositionRequirementAspect extends RequirementsAspect {
    private Position target;
    private ArrayList<ItemSelector> items;

    public ItemsOnPositionRequirementAspect(Action action, Position target, ArrayList<ItemSelector> items) {
        super(action);
        this.target = target;
        this.items = items;
    }

    @Override
    public boolean check() {
        final ArrayList<Item> itemsOnSite = action.getGameContainer().getItemContainer().getItems(target);
        ArrayList<Item> checkedItems = new ArrayList<>();
        for (ItemSelector itemSelector : items) {
            Item selectedItem = itemSelector.selectItems(itemsOnSite);
            if (selectedItem != null) {
                if (!checkedItems.contains(selectedItem)) {
                    checkedItems.add(selectedItem);
                }
            } else {
                return tryCreateHaulingTask(itemSelector);
            }
        }
        return true;
    }

    private boolean tryCreateHaulingTask(ItemSelector item) {
        if (action.getGameContainer().getItemContainer().isItemAvailableFrom(item, target)) {
            Action dropAction = new Action(action.getGameContainer());
            dropAction.setRequirementsAspect(new EquippedItemRequirementAspect(dropAction, ""));
            dropAction.setTargetAspect(new BlockTargetAspect(dropAction, target));
            dropAction.setEffectAspect(new DropItemEffectAspect(dropAction, item));
            return true;
        }
        return false;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
