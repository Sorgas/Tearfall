package stonering.objects.local_actors.unit.aspects;

import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.AspectHolder;
import stonering.objects.local_actors.items.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
    private HashMap<String, EquipmentSlot> slots;                   // equipped items
    private ArrayList<EquipmentSlot> desiredSlots;           // uncovered limbs give comfort penalty
    private HashMap<String, GrabEquipmentSlot> grabSlots;         // equipped items
    private ArrayList<Item> hauledItems;              // hauled items
    private ArrayList<Item> items;                  // for faster checking
    private int hauledItemsLimit;
    private int emptyDesireSlotsCount;

    public EquipmentAspect(AspectHolder aspectHolder) {
        super("equipment", aspectHolder);
        slots = new HashMap<>();
        grabSlots = new HashMap<>();
        items = new ArrayList<>();
        hauledItems = new ArrayList<>();
        desiredSlots = new ArrayList<>();
    }

    /**
     * Equips wear on body and tools into hands.
     *
     * @param item
     * @return
     */
    public boolean equipItem(Item item) {
        //TODO check available slots for item
        //TODO check hauling
        if (item != null && !items.contains(item)) {
            if (item.isWear()) {
                //TODO add wear equiping and counter modifying
                return false;
            } else if (item.isTool()) {
                return tryGrabItem(item);
            }
        }
        return false;
    }

    /**
     * Puts item to hand slot if it is possible.
     *
     * @param item
     * @return
     */
    private boolean tryGrabItem(Item item) {
        for (GrabEquipmentSlot slot : grabSlots.values()) {
            if (slot.grabbedItem == null) {
                slot.grabbedItem = item;
                items.add(item);
                return true;
            }
        }
        GrabEquipmentSlot slotToFree = (GrabEquipmentSlot) selectSlotToFree(true);
        if (freeSlot(slotToFree.limbName, true)) {
            slotToFree.grabbedItem = item;
            items.add(item);
            return true;
        }
        return false;
    }

    public EquipmentSlot selectSlotToFree(boolean grab) {
        if (grab) {
            //TODO add slot selection with some logic
            return grabSlots.values().iterator().next();
        } else {
            //TODO add slot selection with some logic
            return slots.values().iterator().next();
        }
    }

    public boolean freeSlot(String limb, boolean grab) {
        return false;
    }

    public void pickupItem(Item item) {
        tryGrabItem(item);
        //TODO haul in containers
    }

    public List<EquipmentSlot> getEmptyDesiredSlots() {
        if (emptyDesireSlotsCount != 0) {
            return desiredSlots.stream().filter(equipmentSlot -> equipmentSlot.isEmpty()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public class EquipmentSlot {
        public ArrayList<Item> items;
        public String limbName;

        public EquipmentSlot(String limbName) {
            this.limbName = limbName;
            items = new ArrayList<>();
        }

        public boolean isEmpty() {
            return items.isEmpty();
        }
    }

    public class GrabEquipmentSlot extends EquipmentSlot {
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

    public ArrayList<EquipmentSlot> getDesiredSlots() {
        return desiredSlots;
    }

    public void setDesiredSlots(ArrayList<EquipmentSlot> desiredSlots) {
        this.desiredSlots = desiredSlots;
    }

    public int getEmptyDesireSlotsCount() {
        return emptyDesireSlotsCount;
    }

    public void setEmptyDesireSlotsCount(int emptyDesireSlotsCount) {
        this.emptyDesireSlotsCount = emptyDesireSlotsCount;
    }
}
