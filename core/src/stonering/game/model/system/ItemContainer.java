package stonering.game.model.system;

import stonering.entity.Entity;
import stonering.entity.crafting.BuildingComponent;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.enums.items.TagEnum;
import stonering.enums.items.recipe.Ingredient;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.util.global.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages all item on map.
 * //TODO move large methods to some util class
 * //TODO add tracking of equipped item.
 *
 * @author Alexander Kuzyakov on 14.06.2017.
 */
public class ItemContainer extends EntityContainer<Item> {
    private Map<Position, ArrayList<Item>> itemMap;      // maps tiles position to list of item it that position.
    private Map<Item, Entity> containedMap; // maps contained items to containers they are in.

    public ItemContainer() {
        super();
        itemMap = new HashMap<>();
        containedMap = new HashMap<>();
    }

    /**
     * Turns all item for rust, burn, spoil, etc.
     */
    public void turn() {
        entities.forEach(Entity::turn);
    }

    /**
     * Adds item to the global list, and puts to position. Should be used for dropping new items.
     */
    public void addItem(Item item) {
        entities.add(item);
        item.init();
        putItem(item, item.position);
    }

    public void removeItem(Item item) {
        if (!entities.contains(item)) Logger.ITEMS.logWarn("Removing not present item " + item.getName());
        entities.remove(item);
        itemMap.get(item.position).remove(item);
    }

    public void removeItems(List<Item> items) {
        items.forEach(this::removeItem);
    }

    public void moveItem(Item item, Position position) {
        pickItem(item);
        putItem(item, position);
    }

    public void pickItem(Item item) {
        ArrayList<Item> list = itemMap.get(item.position);
        list.remove(item);
        if (list.isEmpty()) {
            itemMap.remove(item.position);
        }
        item.position = null;
    }

    /**
     * Puts item to tile on map. should be used for dropping existent items.
     */
    public void putItem(Item item, Position pos) {
        item.position = pos;
        ArrayList<Item> list = itemMap.get(pos);
        if (list == null) {
            itemMap.put(pos, new ArrayList<>(Arrays.asList(item)));
        } else {
            list.add(item);
        }
    }

    public List<Item> getItemsInPosition(int x, int y, int z) {
        return getItemsInPosition(new Position(x, y, z));
    }

    public List<Item> getItemsInPosition(Position position) {
        List<Item> items = new ArrayList<>();
        if (itemMap.get(position) != null) items.addAll(itemMap.get(position));
        return items;
    }

    /**
     * Gets all materials for all variants of crafting step. Used for filling materialSelectList.
     * Currently works only with resource item.
     */
    public List<Item> getAvailableMaterialsForBuildingStep(BuildingComponent step, Position pos) {
        List<Item> items = new ArrayList<>();
        step.componentVariants.forEach(variant -> {
                    String itemType = variant.itemType;
                    TagEnum tag = TagEnum.get(variant.tag);
                    items.addAll(entities.stream().filter(item -> item.tags.contains(tag) && item.getType().name.equals(itemType)).collect(Collectors.toList()));
                }
        );
        return filterUnreachable(items, pos);
    }

    public List<Item> filterUnreachable(List<Item> items, Position target) {
        return GameMvc.instance().getModel().get(LocalMap.class).getPassage().filterEntitiesByReachability(items, target);
    }

    public boolean hasItemsAvailableBySelector(ItemSelector itemSelector, Position position) {
        return true; //TODO implement item lookup with areas
    }

    public Item getItemAvailableBySelector(ItemSelector itemSelector, Position position) {
        //TODO implement ordering by distance
        List<Item> items = itemSelector.selectItems(entities);
        items = filterUnreachable(items, position);
        if (items.isEmpty()) return null;
        return items.stream().min((item1, item2) -> Math.round(item1.position.getDistanse(position))).get();
    }

    public List<Item> getItemsAvailableBySelector(ItemSelector itemSelector, Position position) {
        List<Item> items = itemSelector.selectItems(entities);
        items = filterUnreachable(items, position);
        if (items == null) Logger.ITEMS.logError("NULL returned instead of empty list");
        return items;
    }

    public List<Item> getNearestItems(List<Item> items, int number) {
        List<Item> result = new ArrayList<>();
        if (number > 0 && items != null && !items.isEmpty()) {
            //TODO
            return items.subList(0, number > items.size() ? items.size() : number);
        }
        return result;
    }

    /**
     * Returns list of items from map, that can be used for given recipe.
     */
    public List<Item> getItemsForIngredient(Ingredient ingredient) {
        return entities.stream().filter(item -> item.tags.contains(ingredient.tag)) // have tag
                .filter(item -> ingredient.itemTypes.contains(item.getType().name)) // appropriate item type
                .collect(Collectors.toList());
    }

    /**
     * Adds item to given container.
     */
    public void addItemToContainer(Item item, Entity container) {
        if (container.hasAspect(ItemContainerAspect.class)) {
            containedMap.containsKey(item);
        }
        Logger.ITEMS.logError("Trying to put item " + item + " into " + container);
    }

    /**
     * Checks that given item exist on map.
     */
    public boolean checkItemList(Collection<Item> items) {
        return items.containsAll(items);
    }

    public boolean checkItem(Item item) {
        return entities.contains(item);
    }
}