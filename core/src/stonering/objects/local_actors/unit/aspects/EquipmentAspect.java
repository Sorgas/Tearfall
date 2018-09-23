package stonering.objects.local_actors.unit.aspects;

import stonering.exceptions.NotSuitableItemException;
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
    private HashMap<String, EquipmentSlot> slots;            // equipped items
    private ArrayList<EquipmentSlot> desiredSlots;           // uncovered limbs give comfort penalty
    private HashMap<String, GrabEquipmentSlot> grabSlots;    // equipped items
    private ArrayList<Item> hauledItems;                     // hauled items
    private ArrayList<Item> items;                           // for faster checking
    private int emptyDesiredSlotsCount;                      // for faster checking nudity

    public EquipmentAspect(AspectHolder aspectHolder) {
        super("equipment", aspectHolder);
        slots = new HashMap<>();
        grabSlots = new HashMap<>();
        items = new ArrayList<>();
        hauledItems = new ArrayList<>();
        desiredSlots = new ArrayList<>();
    }

    /**
     * For hauling items.
     * Validity should be fully checked by actions (slots should be free).
     *
     * @param item
     */
    public void pickupItem(Item item) {
        for (GrabEquipmentSlot slot : grabSlots.values()) {
            if (slot.grabbedItem == null) {
                slot.grabbedItem = item;
                items.add(item);
            }
        }
        //TODO haul in containers
    }

    /**
     * Equips wear on body and tools into hands.
     * Validity should be fully checked by actions (slots should be free).
     *
     * @param item item to equip.
     * @return false, if equipping failed.
     */
    public boolean equipItem(Item item) {
        //TODO check hauling
        if (item != null && !items.contains(item)) {
            if (item.isWear()) { // equip as wear
                List<EquipmentSlot> slots = selectMostEmptySlotsForItem(item);
                for (EquipmentAspect.EquipmentSlot slot : slots) {
                    slot.items.add(item);
                }
                items.add(item);
                return true;
            } else if (item.isTool()) { // grab as tool
                for (GrabEquipmentSlot slot : grabSlots.values()) {
                    if (slot.grabbedItem == null) {
                        slot.grabbedItem = item;
                        items.add(item);
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Pics most unfilled slots with specified limb types.
     *
     * @return list of found slots otherwise.
     */
    private List<EquipmentSlot> selectMostEmptySlotsForItem(Item item) {
        List<EquipmentSlot> slots = new ArrayList<>(this.slots.values());
        List<EquipmentSlot> selectedSlots = new ArrayList<>();
        for (String type : item.getType().getWear().getAllBodyParts()) {
            EquipmentSlot slot;
            if ((slot = selectMostSuitableSlotWithType(slots, type, item.getType().getWear().getLayer())) != null) {
                slots.remove(slot);
                selectedSlots.add(slot);
            }
        }
        return selectedSlots;
    }

    /**
     * Selects slot most suitable for equipping item of given layer.
     *
     * @param slots
     * @param type
     * @return
     */
    private EquipmentSlot selectMostSuitableSlotWithType(List<EquipmentSlot> slots, String type, int layer) {
        //TODO take in account insulation and other properties
        EquipmentSlot emptyestSlot = null;
        for (EquipmentSlot slot : slots) {
            if (slot.limbType.equals(type)) {
                if ((emptyestSlot == null ||
                        (emptyestSlot.isLayerOccupied(layer) && !slot.isLayerOccupied(layer)) ||
                        (emptyestSlot.getItemCountAboveLayer(layer) > slot.getItemCountAboveLayer(layer)) ||
                        (emptyestSlot.getTopLayer() > slot.getTopLayer()))) {
                    emptyestSlot = slot;
                }
            }
        }
        return emptyestSlot;
    }

    /**
     * Checks if given item can be equipped to creature.
     * Checks required and optional slots, demanded by item.
     *
     * @param item
     * @return Item that should be unequipped. nullm if all needed slots are free enough.
     * @throws NotSuitableItemException if ite cannot be equipped.
     */
    public Item checkItemForEquip(Item item) throws NotSuitableItemException {
        //slots with this limb types should exist. types in the list nto unique.
        ArrayList<String> requiredSlotTypes = item.getType().getWear().getRequiredBodyParts();
        if (!checkSlotsWithTypes(requiredSlotTypes)) { // required slots exist on creature
            throw new NotSuitableItemException("Creature " + aspectHolder + " has no required slots for item " + item);
        }
        List<EquipmentSlot> slots = selectMostEmptySlotsForItem(item);
        for (EquipmentAspect.EquipmentSlot slot : slots) {
            Item itemToUnequip = findItemToUnequip(slot, item);
            if (itemToUnequip != null) {
                return itemToUnequip;
            }
        }
        return null;
    }

    /**
     * Checks if for each type from given list exists an least one slot with this type.
     *
     * @param limbTypes
     * @return
     */
    private boolean checkSlotsWithTypes(List<String> limbTypes) {
        limbTypes = new ArrayList<>(limbTypes);
        for (EquipmentSlot slot : slots.values()) {
            limbTypes.remove(slot.limbType);
            if (limbTypes.isEmpty()) {
                break;
            }
        }
        return limbTypes.isEmpty();
    }

    /**
     * Returns one of items from given that should be unequipped to equip given item.
     *
     * @param slot
     * @param item
     * @return
     */
    private Item findItemToUnequip(EquipmentAspect.EquipmentSlot slot, Item item) {
        for (int i = slot.items.size() - 1; i >= 0; i--) {
            if (slot.items.get(i).getType().getWear().getLayer() > item.getType().getWear().getLayer()) {
                slot.items.get(i); // if action possible
            }
        }
        return null; //action not required
    }

    /**
     * Filters and returns slots needed to be filled to avoid nudity penalty.
     *
     * @return
     */
    public List<EquipmentSlot> getEmptyDesiredSlots() {
        if (emptyDesiredSlotsCount != 0) {
            return desiredSlots.stream().filter(equipmentSlot -> equipmentSlot.isEmpty()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public class EquipmentSlot {
        //TODO add multi-item support for one layer
        public ArrayList<Item> items; //lower indexes means item is under other items.
        public String limbName;
        public String limbType;

        public EquipmentSlot(String limbName, String limbType) {
            this.limbName = limbName;
            this.limbType = limbType;
            items = new ArrayList<>();
        }

        public boolean isEmpty() {
            return items.isEmpty();
        }

        public int getTopLayer() {
            return !items.isEmpty() ? items.get(items.size() - 1).getType().getWear().getLayer() : 0;
        }

        public boolean isLayerOccupied(int layer) {
            return items.stream().anyMatch(item -> item.getType().getWear().getLayer() == layer);
        }

        public int getItemCountAboveLayer(int layer) {
            return (int) items.stream().filter(item -> item.getType().getWear().getLayer() > layer).count();
        }
    }

    public class GrabEquipmentSlot extends EquipmentSlot {
        public Item grabbedItem;

        public GrabEquipmentSlot(String limbName, String limbType) {
            super(limbName, limbType);
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

    public int getEmptyDesiredSlotsCount() {
        return emptyDesiredSlotsCount;
    }

    public void setEmptyDesiredSlotsCount(int emptyDesiredSlotsCount) {
        this.emptyDesiredSlotsCount = emptyDesiredSlotsCount;
    }
}
