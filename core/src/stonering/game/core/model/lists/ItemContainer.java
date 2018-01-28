package stonering.game.core.model.lists;

import stonering.global.utils.Position;
import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.items.aspects.PropertyAspect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Alexander on 14.06.2017.
 */
public class ItemContainer {
    private ArrayList<Item> items;
    private HashMap<Position, ArrayList<Item>> itemMap;

    public ItemContainer() {
        items = new ArrayList<>();
        itemMap = new HashMap<>();
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

    public void addItem(Item item) {
        items.add(item);
    }

    public void addItem(Item item, Position position) {
        items.add(item);
        putItem(item, position);
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

    public void turn() {
        items.forEach((item) -> item.turn());
    }

    public Item getItemWithAspect(String aspectName, String aspectArg) {
        for (Item item: items) {
            Aspect aspect = item.getAspects().get(aspectName);
            if(aspect != null && ((PropertyAspect)aspect).hasProperty(aspectArg)) {
                return item;
            }
        }
        return null;
    }
}
