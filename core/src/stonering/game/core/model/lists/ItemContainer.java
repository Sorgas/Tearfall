package stonering.game.core.model.lists;

import stonering.entity.local.crafting.CommonComponentStep;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;
import stonering.global.utils.Position;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.utils.global.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages all items on map.
 *
 * @author Alexander Kuzyakov on 14.06.2017.
 */
public class ItemContainer {
    private GameContainer gameContainer;
    private ArrayList<Item> items;  // all items on the map (tiles, containers, units)
    private HashMap<Position, ArrayList<Item>> itemMap;      // maps tiles position to list of items it that position

    public ItemContainer(ArrayList<Item> items, GameContainer gameContainer) {
        this.gameContainer = gameContainer;
        this.items = new ArrayList<>();
        itemMap = new HashMap<>();
        items.forEach(item -> addItem(item, item.getPosition()));
    }

    public void initItems() {
        items.forEach(this::initItem);
    }

    /**
     * Sets game container to all item aspects
     *
     * @param item
     */
    private void initItem(Item item) {
        item.getAspects().values().forEach((aspect) -> aspect.init(gameContainer));
    }

    /**
     * Turns all items for rust, burn, spoil, etc.
     */
    public void turn() {
        items.forEach((item) -> item.turn());
    }

    public void removeItem(Item item) {
        items.remove(item);
        itemMap.get(item.getPosition()).remove(item);
    }

    public void addItem(Item item, Position position) {
        initItem(item);
        items.add(item);
        putItem(item, position);
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

    public void putItem(Item item, Position pos) {
        item.setPosition(pos);
        ArrayList<Item> list = itemMap.get(pos);
        if (list == null) {
            itemMap.put(pos, new ArrayList<>(Arrays.asList(item)));
        } else {
            list.add(item);
        }
    }

    public ArrayList<Item> getItems(int x, int y, int z) {
        return getItems(new Position(x, y, z));
    }

    public ArrayList<Item> getItems(Position position) {
        if (itemMap.get(position) == null) {
            return new ArrayList<>();
        } else {
            return itemMap.get(position);
        }
    }

    /**
     * Gets all materials for all variants of crafting step. Used for filling materialSelectList.
     * Currently works only with resource items.
     *
     * @param step
     * @param pos
     * @return
     */
    public List<Item> getAvailableMaterialsCraftingStep(CommonComponentStep step, Position pos) {
        List<Item> items = new ArrayList<>();
        step.getVariants().forEach(variant -> {
            items.addAll(getResourceItemListByMaterialType(variant.getMaterial()));
        });
        return filterUnreachable(items, pos);
    }

    /**
     * Searches all material items made of given material type.
     */
    public List<Item> getResourceItemListByMaterialType(String materialType) {
        MaterialMap materialMap = MaterialMap.getInstance();
        List<Item> itemListForFiltering = new ArrayList<>(items);
        Set<Integer> materialIds = materialMap.getMaterialsByType(materialType);
        return itemListForFiltering.stream().
                filter(item -> item.getType().isResource()).
                filter(item -> materialIds.contains(item.getMaterial())).
                collect(Collectors.toList());
    }

    /**
     * Groups given items by material and titles, and count their quantity to show in UI lists.
     */
    public Map<String, Pair<String, String>> formItemListFor(List<Item> items) {
        Map<Pair<String, String>, Integer> groupingMap = new HashMap<>(); // groups by material and item name. Stores quantity.
        items.forEach((item) -> {
            String materialName = MaterialMap.getInstance().getMaterial(item.getMaterial()).getName();
            Pair<String, String> key = new Pair<>(materialName, item.getTitle());
            int prev = groupingMap.containsKey(item.getTitle()) ? groupingMap.get(item.getTitle()) : 0;
            groupingMap.put(key, prev + 1);
        });
        Map<String, Pair<String, String>> resultMap = new HashMap<>();
        groupingMap.keySet().forEach(pair -> {
            String listLine = pair.getKey() + " " + pair.getValue() + " " + groupingMap.get(pair);
            resultMap.put(listLine, pair);
        });
        return resultMap;
    }

    /**
     * Searches items of specified type and material on map.
     *
     * @return
     */
    //TODO use amount
    public List<Item> getItemList(int amount, String itemType, String materialType) {
        Set<Integer> materialIds = MaterialMap.getInstance().getMaterialsByType(materialType);
        List<Item> itemListForFiltering = new ArrayList<>(items);
        return itemListForFiltering.stream().
                filter(item -> item.getType().getTitle().equals(itemType)).
                filter(item -> materialIds.contains(item.getMaterial())).
                collect(Collectors.toList());
    }

    public List<Item> filterUnreachable(List<Item> items, Position pos) {
        LocalMap localMap = gameContainer.getLocalMap();
        return items.stream().filter(item -> localMap.getArea(item.getPosition()) == localMap.getArea(pos)).collect(Collectors.toList());
    }

    /**
     * Checks if item can be reached from position.
     *
     * @param item
     * @param position
     * @return
     */
    public boolean isItemAvailableFrom(Item item, Position position) {
        //TODO implement lookup with areas
        return true;
        //return new AStar(gameContainer.getLocalMap()).makeShortestPath(position, item.getPosition(), true) != null;
    }

    public boolean hasItemsAvailableBySelector(ItemSelector itemSelector, Position position) {
        return true; //TODO implement items lookup with areas
    }

    public Item getItemAvailableBySelector(ItemSelector itemSelector, Position position) {
        //TODO implement ordering by distance
        List<Item> items = itemSelector.selectItems(this.items);
        if (items != null && !items.isEmpty()) { // TODO implement lookup with areas
            return items.get(0);
        }
        return null;
    }
}