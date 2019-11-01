package stonering.game.model.system.item;

import stonering.entity.Entity;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.job.action.Action;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.model.system.EntityContainer;
import stonering.util.geometry.Position;
import stonering.entity.item.Item;
import stonering.util.global.Logger;

import java.util.*;

/**
 * Manages all item in game, including ones in containers, and equipped on units.
 * Items can lay on map tiles, be stored in containers, or equipped by units. There are three separate maps for storing items (for pathfinding).
 * Items have positions, when they are on map. When items are equipped or in containers, their position is null.
 * There are methods for moving items in and out of these maps. Other logic should be made by {@link Action}s or systems.
 * TODO make containers and equipment consistent
 *
 * @author Alexander Kuzyakov on 14.06.2017.
 */
public class ItemContainer extends EntityContainer<Item> {
    public final Map<Position, ArrayList<Item>> itemMap = new HashMap<>(); // maps tiles position to list of item it that position.
    public final Map<Item, ItemContainerAspect> contained = new HashMap<>(); // maps contained items to containers they are in.
    public final Map<Item, EquipmentAspect> equipped = new HashMap<>(); // maps eqiopped and hauled items to units.
    public final Set<Item> lockedItems = new HashSet<>();
    public final ItemStreamUtil util = new ItemStreamUtil(this);

    public void turn() {
        //TODO system for updating equipment
        //TODO system for updating ocntainers
        entities.forEach(Entity::turn);
        //TODO rewrite items aspects to systems
    }

    public void addItem(Item item) {
        entities.add(item);
        item.init();
    }

    /**
     * Registers item in container and puts on map by its position.
     */
    public void addAndPut(Item item) {
        if (item.position == null) Logger.ITEMS.logWarn("Putting item " + item + " with null position.");
        addItem(item);
        putItem(item, item.position);
    }

    public void removeItem(Item item) {
        if (!entities.contains(item)) Logger.ITEMS.logWarn("Removing not present item " + item.getName());
        entities.remove(item);
        pickItem(item);
    }

    public void removeItems(List<Item> items) {
        items.forEach(this::removeItem);
    }

    public void moveItem(Item item, Position position) {
        pickItem(item);
        putItem(item, position);
    }

    public List<Item> getItemsInPosition(int x, int y, int z) {
        return getItemsInPosition(new Position(x, y, z));
    }

    public List<Item> getItemsInPosition(Position position) {
        List<Item> items = new ArrayList<>();
        if (itemMap.get(position) != null) items.addAll(itemMap.get(position));
        return items;
    }

//collection management methods

    public void pickItem(Item item) {
        ArrayList<Item> list = itemMap.get(item.position);
        if (!list.remove(item)) {
            Logger.ITEMS.logWarn("Items inconsistency: item " + item + " is not on the map in position " + item.position);
        }
        if (list.isEmpty()) itemMap.remove(item.position); // last item on the tile
        item.position = null;
    }

    public void putItem(Item item, Position pos) {
        item.position = pos;
        itemMap.putIfAbsent(pos, new ArrayList<>());
        itemMap.get(pos).add(item);
    }

    public void itemAddedToContainer(Item item, ItemContainerAspect aspect) {
        item.position = null;
        aspect.items.add(item);
        contained.put(item, aspect);
    }

    public void itemRemovedFromContainer(Item item, ItemContainerAspect aspect) {
        if (contained.remove(item) == null)
            Logger.ITEMS.logWarn("Items inconsistency: item " + item + " is not registered in ItemContainer as contained");
    }

    public void itemEquipped(Item item, EquipmentAspect aspect) {
        item.position = null;
        equipped.put(item, aspect);
    }

    public void itemUnequipped(Item item) {
        if (equipped.remove(item) == null)
            Logger.ITEMS.logWarn("Items inconsistency: item " + item + " is not registered in ItemContainer as equipped");
    }

    public void freeItems(Collection<Item> items) {
        lockedItems.removeAll(items);
        items.forEach(item -> item.locked = false);
    }
}
