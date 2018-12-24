package stonering.entity.jobs.actions.aspects.requirements;

import stonering.entity.jobs.actions.Action;
import stonering.entity.local.building.Building;
import stonering.entity.local.building.aspects.WorkbenchAspect;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks that specified items are in building container.
 */
public class ItemsInBuildingRequirementAspect extends RequirementsAspect {
    private Building building;
    private List<ItemSelector> itemSelectors;

    public ItemsInBuildingRequirementAspect(Action action, Building building) {
        super(action);
        this.building = building;
    }

    @Override
    public boolean check() {
        if (!building.getAspects().containsKey(WorkbenchAspect.NAME)) return false;
        List<Item> storage = ((WorkbenchAspect) building.getAspects().get(WorkbenchAspect.NAME)).getStorage();
        List<Item> items = new ArrayList<>();
        items.addAll(storage);
        for (ItemSelector itemSelector : itemSelectors) {
            List<Item> selectedItems = itemSelector.selectItems(items);
            if (!selectedItems.isEmpty()) {
                items.removeAll(selectedItems);
            } else {
                return false;
            }
        }
        return true;
    }
}
