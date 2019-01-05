package stonering.game.core.model.lists;

import stonering.entity.local.crafting.CommonComponentStep;
import stonering.entity.local.items.selectors.SimpleItemSelector;
import stonering.enums.items.recipe.ItemPartRecipe;
import stonering.enums.items.recipe.Recipe;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;
import stonering.global.utils.Position;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.util.global.Pair;

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
            items.addAll(getResourceItemsByMaterialType(variant.getMaterial()));
        });
        return filterUnreachable(items, pos);
    }

    /**
     * Searches all material items made of given material type.
     */
    public List<Item> getResourceItemsByMaterialType(String materialType) {
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
    public List<ItemGroup> groupItemsByTypesAndMaterials(List<Item> items) {
        Map<ItemGroup, Integer> groupingMap = new HashMap<>();                                   // groups by material and item NAME. Stores quantity.
        items.forEach((item) -> {
            String materialName = MaterialMap.getInstance().getMaterial(item.getMaterial()).getName();
            ItemGroup key = new ItemGroup(item.getTitle(), materialName, 0);
            groupingMap.put(key, groupingMap.getOrDefault(key, 0) + 1);                          // increment quantity
        });
        List<ItemGroup> resultList = new ArrayList<>();
        groupingMap.keySet().forEach(group -> {
            group.quantity = groupingMap.get(group);
            resultList.add(group);
        });
        return resultList;
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
        items = filterUnreachable(items, position);
        if (items != null && !items.isEmpty()) {
            //TODO return nearest items
            return items.get(0);
        }
        return null;
    }

    /**
     * Gets item selectors for items, suitable for recipe and available from given position.
     */
    public List<ItemSelector> getItemSelectorsForItemPartRecipe(ItemPartRecipe itemPartRecipe, Position position) {
        List<ItemSelector> itemSelectors = new ArrayList<>();
        Set<Integer> allowedMaterials = MaterialMap.getInstance().getMaterialsByType(itemPartRecipe.getMaterialType());
        List<Item> materialItems = items.stream().filter(item -> item.getType().isResource() && allowedMaterials.contains(item.getMaterial())).collect(Collectors.toList());
        materialItems = filterUnreachable(materialItems, position);
        for (ItemGroup itemGroup : groupItemsByTypesAndMaterials(materialItems)) {
            itemSelectors.add(createItemSelector(itemGroup));
        }
        return itemSelectors;
    }

    private ItemSelector createItemSelector(ItemGroup itemGroup) {
        return new SimpleItemSelector(itemGroup.type, itemGroup.material, 1);
    }

    /**
     * Groups items by type and material. Stores quantity.
     */
    private class ItemGroup {
        String type;
        String material;
        int quantity;

        public ItemGroup(String type, String material, int quantity) {
            this.type = type;
            this.material = material;
            this.quantity = quantity;
        }
    }
}