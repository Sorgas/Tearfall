package stonering.game.model.system.item;

import stonering.entity.item.Item;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.EntitySystem;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

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

    }

    public void putItem(Item item, Position pos) {
        item.position = pos;
        container.itemMap.putIfAbsent(pos, new ArrayList<>());
        container.itemMap.get(pos).add(item);
    }

    public void removeItemFromMap(Item item) {
        List<Item> list = container.itemMap.get(item.position);
        if (!list.remove(item)) Logger.ITEMS.logWarn("Items inconsistency: item " + item + " is not on the map in position " + item.position);
        if (list.isEmpty()) container.itemMap.remove(item.position); // last item on the tile
        item.position = null;
    }

    public void changeItemPosition(Item item, Position position) {
        removeItemFromMap(item);
        putItem(item, position);
    }

    /**
     * Adds item to container and then puts it to given position.
     */
    public void putNewItem(Item item, Position position) {
       container.addItem(item);
       putItem(item, position);
    }
}
