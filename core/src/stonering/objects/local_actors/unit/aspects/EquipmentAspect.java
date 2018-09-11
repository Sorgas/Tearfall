package stonering.objects.local_actors.unit.aspects;

import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.AspectHolder;
import stonering.objects.local_actors.items.Item;

import java.util.ArrayList;

/**
 * Stores all items equipped and hauled by unit.
 * Equipped items are ones, weared on body.
 *
 * Does not takes or puts items to map, this should be done by actions.
 *
 * @author Alexander on 03.01.2018.
 * <p>
 */
public class EquipmentAspect extends Aspect {
    private ArrayList<Item> items;
    private ArrayList<Item> inventory;

    public EquipmentAspect(AspectHolder aspectHolder) {
        super("equipment", aspectHolder);
        items = new ArrayList<>();
        inventory = new ArrayList<>();
    }

    public Item getItemWithAspectAndProperty(String property) {
        for (Item item : items) {
            if (item.getType().getAspects().containsKey(property))
                return item;
        }
        return null;
    }

    public void equipItem(Item item, boolean allowHauling) {
        //TODO check available slots for item
        //TODO check hauling
        if (true) {
            items.add(item);
        }
    }

    public void unequipItem(Item item) {
        items.remove(item);
    }

    public boolean checkItem(Item item) {
        return items.contains(item) || inventory.contains(item);
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void pickupItem(Item item) {
        inventory.add(item);
    }

    public void dropItem(Item item) {
        inventory.remove(item);
    }
}
