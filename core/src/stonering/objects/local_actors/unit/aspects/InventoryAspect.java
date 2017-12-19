package stonering.objects.local_actors.unit.aspects;

import stonering.objects.local_actors.items.Item;

import java.util.LinkedList;

public class InventoryAspect {
    private LinkedList<Item> items;

    public LinkedList<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        if(!items.contains(item))
            items.add(item);
    }

    public LinkedList<Item> getItems(String type) {
        return null;
    }
}
