package stonering.game.model.lists;

import stonering.entity.local.Entity;
import stonering.entity.local.crafting.CommonComponent;
import stonering.entity.local.item.aspects.ResourceAspect;
import stonering.entity.local.item.selectors.SimpleItemSelector;
import stonering.enums.items.recipe.ItemPartRecipe;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.ModelComponent;
import stonering.game.model.Turnable;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.util.UtilByteArray;
import stonering.util.geometry.Position;
import stonering.entity.local.item.Item;
import stonering.entity.local.item.selectors.ItemSelector;
import stonering.util.global.Initable;
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
public class ItemContainer extends Turnable implements ModelComponent, Initable {
    private ArrayList<Item> items;  // all item on the map (tiles, containers, units)
    private HashMap<Position, ArrayList<Item>> itemMap;      // maps tiles position to list of item it that position

    public ItemContainer() {
        items = new ArrayList<>();
        itemMap = new HashMap<>();
    }

    @Override
    public void init() {
        items.forEach(Item::init);

    }

    /**
     * Turns all item for rust, burn, spoil, etc.
     */
    public void turn() {
        items.forEach(Entity::turn);
    }

    /**
     * For initial item placing
     */
    public ItemContainer placeItems(List<Item> items) {
        items.forEach(item -> {
            this.items.add(item);
            putItem(item, item.getPosition());
        });
        return this;
    }

    public void removeItem(Item item) {
        if (!items.contains(item)) Logger.ITEMS.logWarn("Removing not present item " + item.getName());
        items.remove(item);
        itemMap.get(item.getPosition()).remove(item);
    }

    public void removeItems(List<Item> items) {
        items.forEach(this::removeItem);
    }

    public void addItem(Item item) {
        items.add(item);
        item.init();
        putItem(item, item.getPosition());
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
     *
     * @param step
     * @param pos
     * @return
     */
    public List<Item> getAvailableMaterialsCraftingStep(CommonComponent step, Position pos) {
        List<Item> items = new ArrayList<>();
        step.getComponentVariants().forEach(variant -> items.addAll(getResourceItemsByMaterialType(variant.getTag())));
        return filterUnreachable(items, pos);
    }

    /**
     * Searches all material item made of given material type.
     */
    public List<Item> getResourceItemsByMaterialType(String materialType) {
        MaterialMap materialMap = MaterialMap.getInstance();
        List<Item> itemListForFiltering = new ArrayList<>(items);
        Set<Integer> materialIds = materialMap.getMaterialsByType(materialType);
        return itemListForFiltering.stream().
                filter(item -> item.getType().hasAspect(ResourceAspect.class)).
                filter(item -> materialIds.contains(item.getMaterial())).
                collect(Collectors.toList());
    }

    /**
     * Groups given item by material and titles, and count their quantity to show in UI lists.
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

    //TODO carried item have no position
    public List<Item> filterUnreachable(List<Item> items, Position pos) {
        UtilByteArray area = GameMvc.instance().getModel().get(LocalMap.class).getPassage().getArea();
        return items.stream().filter(item -> item.getPosition() != null && area.getValue(item.getPosition()) == area.getValue(pos)).collect(Collectors.toList());
    }

    public boolean hasItemsAvailableBySelector(ItemSelector itemSelector, Position position) {
        return true; //TODO implement item lookup with areas
    }

    public Item getItemAvailableBySelector(ItemSelector itemSelector, Position position) {
        //TODO implement ordering by distance
        List<Item> items = itemSelector.selectItems(this.items);
        items = filterUnreachable(items, position);
        if (items != null && !items.isEmpty()) {
            //TODO return nearest item
            return items.get(0);
        }
        return null;
    }

    public List<Item> getItemsAvailableBySelector(ItemSelector itemSelector, Position position) {
        List<Item> items = itemSelector.selectItems(this.items);
        items = filterUnreachable(items, position);
        if (items == null) {
            Logger.ITEMS.logError("NULL returned instead of empty list");
        }
        return items;
    }

    /**
     * Gets item selectors for item, suitable for recipe and available from given position.
     */
    public List<ItemSelector> getItemSelectorsForItemPartRecipe(ItemPartRecipe itemPartRecipe, Position position) {
        Set<ItemSelector> itemSelectors = new HashSet<>();
        Set<Integer> allowedMaterials = MaterialMap.getInstance().getMaterialsByType(itemPartRecipe.materialTag);
        List<Item> materialItems = items.stream().
                filter(item -> item.getType().hasAspect(ResourceAspect.class)).
                filter(item -> allowedMaterials.contains(item.getMaterial())).
                collect(Collectors.toList());
        materialItems = filterUnreachable(materialItems, position); // TODO carried item has no position giving NPE
        for (ItemGroup itemGroup : groupItemsByTypesAndMaterials(materialItems)) {
            itemSelectors.add(createItemSelector(itemGroup));
        }
        return new ArrayList<>(itemSelectors);
    }

    private ItemSelector createItemSelector(ItemGroup itemGroup) {
        return new SimpleItemSelector(itemGroup.type, itemGroup.material, 1);
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
        return items.contains(item);
    }

    /**
     * Groups item by type and material. Stores quantity.
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