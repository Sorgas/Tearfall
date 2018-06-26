package stonering.game.core.model.lists;

import stonering.game.core.model.GameContainer;
import stonering.global.utils.Position;
import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.items.aspects.TagAspect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Manages all items on map
 * <p>
 * @author Alexander Kuzyakov on 14.06.2017.
 */
public class ItemContainer {
    private ArrayList<Item> items;
    private HashMap<Position, ArrayList<Item>> itemMap;
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
            if (item.getType().getProperties().containsKey(property))
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
}
