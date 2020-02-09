package stonering.game.model.system.item;

import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.item.Item;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.EntitySystem;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * System for updating items stored in workbenches and containers.
 * Methods should be called in pairs with other item util methods.
 *
 * @author Alexander on 17.11.2019.
 */
public class ContainedItemsSystem extends EntitySystem<Item> {
    private ItemContainer container;

    public ContainedItemsSystem(ItemContainer container) {
        this.container = container;
        updateInterval = TimeUnitEnum.MINUTE;
    }


    @Override
    public void update(Item entity) {

    }

    /**
     * Adds item to wb. Item should be removed from map and units.
     */
    public void addItemToWorkbench(Item item, WorkbenchAspect aspect) {
        if(container.equipped.containsKey(item)) Logger.ITEMS.logError("Adding to wb item not removed from unit");
        if(item.position != null) Logger.ITEMS.logError("Adding to wb item not removed from map");
        aspect.containedItems.add(item);
        container.contained.put(item, aspect);
    }

    /**
     * Remove item from wb. Item should be deleted from map or put somewhere after this.
     */
    public void removeItemFromWorkbench(Item item, WorkbenchAspect aspect) {
        if(!aspect.containedItems.remove(item))
            Logger.ITEMS.logWarn("Items inconsistency: item " + item + " is not stored in wb aspect");
        if (container.contained.remove(item) == null)
            Logger.ITEMS.logWarn("Items inconsistency: item " + item + " is not registered in ItemContainer as contained");
    }

    public void removeItemsFromWorkbench(List<Item> items, WorkbenchAspect aspect) {
        for (Item item : new ArrayList<>(items)) {
            removeItemFromWorkbench(item, aspect);
        }
    }

    public boolean isItemContained(Item item) {
        return container.contained.containsKey(item);
    }
}
