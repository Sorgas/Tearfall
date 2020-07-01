package stonering.game.model.system.item;

import stonering.entity.item.Item;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.EntitySystem;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates items on map.
 * Methods for adding and removing should be called in pair with other methods.
 *
 * @author Alexander on 17.11.2019.
 */
public class OnMapItemsSystem extends EntitySystem<Item> {
    private ItemContainer container;

    public OnMapItemsSystem(ItemContainer container) {
        this.container = container;
        updateInterval = TimeUnitEnum.MINUTE;
    }

    @Override
    public void update(Item entity) {
        // update items
    }

    public void addItemToMap(Item item, Position position) {
        if(item == null) return;
        Logger.ITEMS.logDebug("Putting item " + item + " to map");
        validateBeforeAdding(item);
        item.position = position;
        container.itemMap.computeIfAbsent(position, pos -> new ArrayList<>()).add(item);
        container.onMapItemsSet.add(item);
    }

    public void removeItemFromMap(Item item) {
        Logger.ITEMS.logDebug("Removing item " + item + " from map");
        validateBeforeRemoving(item);
        List<Item> list = container.itemMap.get(item.position);
        list.remove(item);
        if (list.isEmpty()) container.itemMap.remove(item.position); // last item on the tile
        container.onMapItemsSet.remove(item);
        item.position = null;
    }

    public void changeItemPosition(Item item, Position position) {
        removeItemFromMap(item);
        addItemToMap(item, position);
    }

    /**
     * Adds item to container and then puts it to given position.
     */
    public void addNewItemToMap(Item item, Position position) {
       container.addItem(item);
       addItemToMap(item, position);
    }

    private void validateBeforeAdding(Item item) {
        validateOther(item);
        if(container.isItemOnMap(item)) Logger.ITEMS.logError("Items inconsistency: item " + item + " is already on map");
    }

    private void validateBeforeRemoving(Item item) {
        validateOther(item);
        if(!container.isItemOnMap(item)) Logger.ITEMS.logError("Items inconsistency: item " + item + " is not on map");
    }

    private void validateOther(Item item) {
        if(container.isItemEquipped(item)) Logger.ITEMS.logError("Items inconsistency: item " + item + " is equipped");
        if(container.isItemInContainer(item)) Logger.ITEMS.logError("Items inconsistency: item " + item + " is in container");
    }
}
