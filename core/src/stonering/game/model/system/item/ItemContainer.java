package stonering.game.model.system.item;

import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.job.action.Action;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.model.system.EntityContainer;
import stonering.util.geometry.Position;
import stonering.entity.item.Item;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public final Map<Position, List<Item>> itemMap = new HashMap<>(); // maps tiles position to list of item it that position.
    public final Map<Item, WorkbenchAspect> contained = new HashMap<>(); // maps contained items to containers they are in. // todo replace to container aspect
    public final Map<Item, EquipmentAspect> equipped = new HashMap<>(); // maps equipped and hauled items to units.
    public final Set<Item> lockedItems = new HashSet<>();
    public final ItemStreamUtil util = new ItemStreamUtil(this);

    public final ContainedItemsSystem containedItemsSystem;
    public final EquippedItemsSystem equippedItemsSystem;
    public final OnMapItemsSystem onMapItemsSystem;

    public ItemContainer() {
        putSystem(containedItemsSystem = new ContainedItemsSystem(this));
        putSystem(equippedItemsSystem = new EquippedItemsSystem(this));
        putSystem(onMapItemsSystem = new OnMapItemsSystem(this));
    }

    //TODO system for updating containers
    //TODO rewrite items aspects to systems

    /**
     * Adds item to container and inits it's aspects. Used for registering newly created items.
     * After that item should be put somewhere.
     */
    public void addItem(Item item) {
        entities.add(item);
        item.init(); // init item aspects
    }

    /**
     * Removes item from the game completely. Item should be removed from all other maps before this.
     */
    public void removeItem(Item item) {
        if (!entities.contains(item)) Logger.ITEMS.logWarn("Removing not present item " + item.type.name);
        entities.remove(item);
    }

    public void removeItems(List<Item> items) {
        items.forEach(this::removeItem);
    }

    public List<Item> getItemsInPosition(Position position) {
        return new ArrayList<>(itemMap.getOrDefault(position, Collections.emptyList()));
    }

    public List<Item> getItemsInPosition(int x, int y, int z) {
        return getItemsInPosition(new Position(x, y, z));
    }

    public void freeItems(Collection<Item> items) { //TODO
        lockedItems.removeAll(items);
        items.forEach(item -> item.locked = false);
    }
}
