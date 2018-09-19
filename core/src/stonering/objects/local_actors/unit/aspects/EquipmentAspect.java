package stonering.objects.local_actors.unit.aspects;

import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.AspectHolder;
import stonering.objects.local_actors.items.Item;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stores all items equipped and hauled by unit.
 * Equipped items are ones, weared on body.
 * <p>
 * Does not takes or puts items to map, this should be done by actions.
 *
 * @author Alexander on 03.01.2018.
 *         <p>
 */
public class EquipmentAspect extends Aspect {
    private HashMap<String, EquipmentSlot> slots;         // equipped items
    private HashMap<String, GrabEquipmentSlot> grabSlots;         // equipped items
    private ArrayList<Item> hauledItems;              // hauled items
    private ArrayList<Item> items;                  // for faster checking
    private int hauledItemsLimit;


    public EquipmentAspect(AspectHolder aspectHolder) {
        super("equipment", aspectHolder);
        slots = new HashMap<>();
        grabSlots = new HashMap<>();
        items = new ArrayList<>();
        hauledItems = new ArrayList<>();
    }

    public Item getItemWithAspectAndProperty(String property) {
//        for (Item item : items.values()) {
//            if (item.getType().getAspects().containsKey(property))
//                return item;
//        }
        return null;
    }

    public void equipItem(Item item, boolean allowHauling) {
        //TODO check available slots for item
        //TODO check hauling
        if (true) {
//            items.add(item);
        }
    }

    public class EquipmentSlot {
        public ArrayList<Item> items;
        public String limbName;

        public EquipmentSlot(String limbName) {
            this.limbName = limbName;
            items = new ArrayList<>();
        }
    }

    public class GrabEquipmentSlot extends EquipmentSlot{
        public Item grabbedItem;

        public GrabEquipmentSlot(String limbName) {
            super(limbName);
        }
    }

    public void unequipItem(Item item) {
        items.remove(item);
    }

    public boolean checkItem(Item item) {
        return items.contains(item) || hauledItems.contains(item);
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<Item> getHauledItems() {
        return hauledItems;
    }

    public void pickupItem(Item item) {
        hauledItems.add(item);
    }

    public void dropItem(Item item) {
        hauledItems.remove(item);
    }

    public int getHauledItemsLimit() {
        return hauledItemsLimit;
    }

    public void setHauledItemsLimit(int hauledItemsLimit) {
        this.hauledItemsLimit = hauledItemsLimit;
    }

    public HashMap<String, EquipmentSlot> getSlots() {
        return slots;
    }

    public void setSlots(HashMap<String, EquipmentSlot> slots) {
        this.slots = slots;
    }

    public HashMap<String, GrabEquipmentSlot> getGrabSlots() {
        return grabSlots;
    }

    public void setGrabSlots(HashMap<String, GrabEquipmentSlot> grabSlots) {
        this.grabSlots = grabSlots;
    }
}
