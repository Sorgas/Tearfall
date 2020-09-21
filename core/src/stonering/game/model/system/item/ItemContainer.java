package stonering.game.model.system.item;

import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.job.action.Action;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.EntityContainer;
import stonering.util.geometry.Position;
import stonering.entity.item.Item;
import stonering.util.geometry.PositionUtil;
import stonering.util.logging.Logger;

import java.util.*;

/**
 * Manages all items in game, including ones in containers, and equipped on units.
 * Items can lay on map tiles, be stored in containers, or equipped by units. There are three separate maps for storing items (for pathfinding).
 * When item is equipped, its position is null.
 * There are methods for moving items in and out of these maps. Other logic should be made by {@link Action}s or systems.
 * TODO make containers and equipment consistent
 *
 * @author Alexander Kuzyakov on 14.06.2017.
 */
public class ItemContainer extends EntityContainer<Item> {
    public final Map<Position, List<Item>> itemMap = new HashMap<>(); // maps tiles position to list of item it that position.
    public final Set<Item> onMapItemsSet = new HashSet<>(); // set for faster checking
    public final Map<Item, ItemContainerAspect> contained = new HashMap<>(); // maps contained items to containers they are in.
    public final Map<Item, EquipmentAspect> equipped = new HashMap<>(); // maps equipped and hauled items to units.

    public final ItemStreamUtil util = new ItemStreamUtil(this);

    public final ContainedItemsSystem containedItemsSystem;
    public final EquippedItemsSystem equippedItemsSystem;
    public final OnMapItemsSystem onMapItemsSystem;
    private Position cachePosition;
    private LocalMap map;

    public ItemContainer() {
        addSystem(containedItemsSystem = new ContainedItemsSystem(this));
        addSystem(equippedItemsSystem = new EquippedItemsSystem(this));
        addSystem(onMapItemsSystem = new OnMapItemsSystem(this));
        cachePosition = new Position();
    }

    public void addItem(Item item) {
        objects.add(item);
    }

    public void removeItem(Item item) {
        if (!objects.contains(item)) {
            Logger.ITEMS.logWarn("Removing not present item " + item.type.name);
        } else {
            Logger.ITEMS.logDebug("Removing item " + item.type.name);
        }
        if (isItemOnMap(item)) onMapItemsSystem.removeItemFromMap(item);
        if (isItemInContainer(item)) containedItemsSystem.removeItemFromContainer(item);
        if (isItemEquipped(item)) equippedItemsSystem.removeItemFromEquipment(item);
        item.destroyed = true;
        objects.remove(item);
    }

    public void removeItems(List<Item> items) {
        items.forEach(this::removeItem);
    }

    public List<Item> getItemsInPosition(Position position) {
        return new ArrayList<>(itemMap.getOrDefault(position, Collections.emptyList()));
    }

    public List<Item> getItemsInPosition(int x, int y, int z) {
        return getItemsInPosition(cachePosition.set(x, y, z));
    }

    public boolean itemAccessible(Item item, Position position) {
        //TODO handle items in containers
        if(isItemInContainer(item)) {
            Position containerPosition = contained.get(item).entity.position;
            LocalMap map = map();
            byte area = map.passageMap.area.get(position);
            return PositionUtil.allNeighbour.stream()
                    .map(pos -> Position.add(pos, containerPosition))
                    .filter(map::inMap)
                    .map(map.passageMap.area::get)
                    .anyMatch(area1 -> area1 == area);
        }
        return item.position != null && map().passageMap.area.get(position) == map().passageMap.area.get(item.position);
    }

    private LocalMap map() {
        return map == null ? map = GameMvc.model().get(LocalMap.class) : map;
    }

    public boolean isItemOnMap(Item item) {
        return onMapItemsSet.contains(item);
    }

    public boolean isItemInContainer(Item item) {
        return contained.containsKey(item);
    }

    public boolean isItemEquipped(Item item) {
        return equipped.containsKey(item);
    }
}
