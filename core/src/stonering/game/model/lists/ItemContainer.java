package stonering.game.model.lists;

import stonering.entity.Entity;
import stonering.entity.crafting.CommonComponent;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.util.UtilByteArray;
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
    private HashMap<Position, ArrayList<Item>> itemMap;      // maps tiles position to list of item it that position

    public ItemContainer() {
        super();
        itemMap = new HashMap<>();
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
        putItem(item, item.getPosition());
    }

    public void removeItem(Item item) {
        if (!entities.contains(item)) Logger.ITEMS.logWarn("Removing not present item " + item.getName());
        entities.remove(item);
        itemMap.get(item.getPosition()).remove(item);
    }

    public void removeItems(List<Item> items) {
        items.forEach(this::removeItem);
    }

    public void moveItem(Item item, Position position) {
        pickItem(item);
        putItem(item, position);
    }

    public void pickItem(Item item) {
        ArrayList<Item> list = itemMap.get(item.getPosition());
        list.remove(item);
        if (list.isEmpty()) {
            itemMap.remove(item.getPosition());
        }
        item.setPosition(null);
    }

    /**
     * Puts item to tile on map. should be used for dropping existent items.
     */
    public void putItem(Item item, Position pos) {
        item.setPosition(pos);
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
    public List<Item> getAvailableMaterialsCraftingStep(CommonComponent step, Position pos) {
        List<Item> items = new ArrayList<>();
        step.getComponentVariants().forEach(variant -> items.addAll(getResourceItemsByMaterialType(variant.getTag())));
        return filterUnreachable(items, pos);
    }

    /**
     * Searches all material item made of given material type.
     */
    private List<Item> getResourceItemsByMaterialType(String materialType) {
        MaterialMap materialMap = MaterialMap.instance();
        List<Item> itemListForFiltering = new ArrayList<>(entities);
        Set<Integer> materialIds = materialMap.getMaterialsByTag(materialType);
        return itemListForFiltering.stream().
                filter(item -> materialIds.contains(item.getMaterial())).
                collect(Collectors.toList());
    }

    public List<Item> filterUnreachable(List<Item> items, Position fromPosition) {
        UtilByteArray area = GameMvc.instance().getModel().get(LocalMap.class).getPassage().getArea();
        return items.stream().
                filter(item -> item.getPosition() != null).
                filter(item -> area.getValue(item.getPosition()) == area.getValue(fromPosition)).
                collect(Collectors.toList());
    }

    public boolean hasItemsAvailableBySelector(ItemSelector itemSelector, Position position) {
        return true; //TODO implement item lookup with areas
    }

    public Item getItemAvailableBySelector(ItemSelector itemSelector, Position position) {
        //TODO implement ordering by distance
        List<Item> items = itemSelector.selectItems(entities);
        items = filterUnreachable(items, position);
        if (items.isEmpty()) return null;
        return items.stream().min((item1, item2) -> Math.round(item1.getPosition().getDistanse(position))).get();
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
     * Checks that given item exist on map.
     */
    public boolean checkItemList(Collection<Item> items) {
        return items.containsAll(items);
    }

    public boolean checkItem(Item item) {
        return entities.contains(item);
    }
}