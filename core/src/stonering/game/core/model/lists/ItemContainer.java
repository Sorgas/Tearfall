package stonering.game.core.model.lists;

import stonering.enums.buildings.BuildingMap;
import stonering.enums.buildings.BuildingType;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;
import stonering.global.utils.Position;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages all items on map
 * <p>
 * @author Alexander Kuzyakov on 14.06.2017.
 */
public class ItemContainer {
    private ArrayList<Item> items;
    private HashMap<Position, ArrayList<Item>> itemMap;
    private ArrayList<Item> lockedItems;
    private GameContainer gameContainer;

    public ItemContainer(ArrayList<Item> items, GameContainer gameContainer) {
        this.gameContainer = gameContainer;
        this.items = new ArrayList<>();
        itemMap = new HashMap<>();
        items.forEach(item -> addItem(item, item.getPosition()));
    }

    public void initItems() {
        items.forEach(this::initItem);
    }

    private void initItem(Item item) {
        item.getAspects().values().forEach((aspect) -> aspect.init(gameContainer));
    }

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
        if(list.isEmpty()) {
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

    public Item getItemWithProperty(String property) {
        for (Item item : items) {
            if (item.getType().getAspects().containsKey(property))
                return item;
        }
        return null;
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

    public List<Item> getAvailableMaterialsForBuilding(String buildingTitle, Position pos) {
        BuildingType buildingType = BuildingMap.getInstance().getBuilding(buildingTitle);
        if (buildingType != null) {
            List<Item> items = new ArrayList<>(getMaterialList(buildingType.getAmount(), buildingType.getItems(), buildingType.getMaterials()));
            return filterUnreachable(items, pos);
//            HashMap<Pair<String, String>, Integer> map = new HashMap<>();
//                    .forEach((item) -> {
//                Pair<String, String> key = new Pair<>(materialMap.getMaterial(item.getMaterial()).getName(), item.getTitle());
//                int prev = map.containsKey(item.getTitle()) ? map.get(item.getTitle()) : 0;
//                map.put(key, prev + 1);
//            });
//            return map;
        }
        return new ArrayList<>();
    }

    public List<Item> getMaterialList(int amount, ArrayList<String> types, ArrayList<String> materialTypes) {
        MaterialMap materialMap = MaterialMap.getInstance();
        HashSet<Integer> materialIds = materialMap.getMaterialsByTypes(materialTypes);
        ArrayList<Item> itemListForFiltering = new ArrayList<>(items);
        return itemListForFiltering.stream().
                filter(item -> types.contains(item.getType().getTitle())).
                filter(item -> materialIds.contains(item.getMaterial())).
                collect(Collectors.toList());
    }

    public List<Item> filterUnreachable(List<Item> items, Position pos) {
        LocalMap localMap = gameContainer.getLocalMap();
        return items.stream().filter(item -> localMap.getArea(item.getPosition()) == localMap.getArea(pos)).collect(Collectors.toList());
    }

    private ArrayList<Item> filterItemListForMaterials(String Material, ArrayList<Item> items) {
        return null;
    }

    /**
     * Checks if item can be reached from position.
     * @param item
     * @param position
     * @return
     */
    public boolean isItemAvailableFrom(Item item, Position position) {
        //TODO implement lookup with areas
        return true;
        //return new AStar(gameContainer.getLocalMap()).makeShortestPath(position, item.getPosition(), true) != null;
    }

    public void lockItem(Item item) {
        if(items.contains(item)) {
            items.remove(item);
            lockedItems.add(item);
        }
    }

    public void unlockItem(Item item) {
        if(lockedItems.contains(item)) {
            lockedItems.remove(item);
            items.add(item);
        }
    }

    public boolean hasItemsAvailableBySelector(ItemSelector itemSelector, Position position) {
        return true; //TODO implement items lookup with areas
    }

    public Item getItemAvailableBySelector(ItemSelector itemSelector, Position position) {
        //TODO implement ordering by distance
        List<Item> items = itemSelector.selectItems(this.items);
        if(items != null && !items.isEmpty()) { // TODO implement lookup with areas
            return items.get(0);
        }
        return null;
    }

    public boolean isItemLocked(Item item) {
        return lockedItems.contains(item);
    }
}