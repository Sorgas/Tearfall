package stonering.entity.jobs.actions.aspects.requirements;

import stonering.game.core.model.lists.ItemContainer;
import stonering.global.utils.Position;
import stonering.entity.jobs.actions.Action;
import stonering.entity.jobs.actions.aspects.effect.DropItemEffectAspect;
import stonering.entity.jobs.actions.aspects.target.BlockTargetAspect;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.unit.aspects.EquipmentAspect;

import java.util.ArrayList;

/**
 * Checks if specific itemSelectors are on desired position or in the inventory of action performer.
 * Creates dropping and picking actions if needed.
 *
 * @author Alexander on 08.07.2018.
 */
public class ItemsInPositionOrInventoryRequirementAspect extends RequirementsAspect {
    private Position target;
    private ArrayList<ItemSelector> itemSelectors;

    public ItemsInPositionOrInventoryRequirementAspect(Action action, Position target, ArrayList<ItemSelector> itemSelectors) {
        super(action);
        this.target = target;
        this.itemSelectors = itemSelectors;
    }

    @Override
    public boolean check() {
        ArrayList<Item> uncheckedItems = new ArrayList<>();
        ArrayList<Item> checkedItems = new ArrayList<>();
        uncheckedItems.addAll(action.getGameContainer().getItemContainer().getItems(target)); // from target position
        uncheckedItems.addAll(((EquipmentAspect)action.getTask().getPerformer().getAspects().get("equipment")).getHauledItems()); // from performer inventory
        for (ItemSelector itemSelector : itemSelectors) {
            ArrayList<Item> selectedItems = itemSelector.selectItems(uncheckedItems);
            if (!selectedItems.isEmpty()) {
                for (int i = 0; i < selectedItems.size(); i++) {
                    Item item = selectedItems.get(i);
                    if (!checkedItems.contains(item)) {
                        checkedItems.add(item);
                        uncheckedItems.remove(item);
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
                dropAction.setTargetAspect(new BlockTargetAspect(dropAction, target, true, false));
                dropAction.setEffectAspect(new DropItemEffectAspect(dropAction, item));
                action.getTask().addFirstPreAction(dropAction);
                return true;
            }
        }
        return false;
    }

    public ArrayList<ItemSelector> getItemSelectors() {
        return itemSelectors;
    }
}
