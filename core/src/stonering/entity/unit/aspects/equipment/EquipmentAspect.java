package stonering.entity.unit.aspects.equipment;

import stonering.entity.Entity;
import stonering.exceptions.NotSuitableItemException;
import stonering.entity.Aspect;
import stonering.entity.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stores all item equipped and hauled by unit.
 * Equipped item are ones, worn on body.
 * Does not takes or puts item to map, this should be done by actions.
 *
 * @author Alexander Kuzyakov on 03.01.2018.
 */
public class EquipmentAspect extends Aspect {
    public static String NAME = "equipment";
    private HashMap<String, EquipmentSlot> slots;            // all slots of a creature
    private HashMap<String, GrabEquipmentSlot> grabSlots;    // equipped item
    private ArrayList<Item> hauledItems;                     // hauled item list for faster checking
    private ArrayList<Item> equippedItems;                   // equipped item list for faster checking
    private ArrayList<EquipmentSlot> desiredSlots;           // uncovered limbs give comfort penalty
    private int emptyDesiredSlotsCount;                      // for faster checking nudity

    public EquipmentAspect(Entity entity) {
        super(entity);
        slots = new HashMap<>();
        grabSlots = new HashMap<>();
        equippedItems = new ArrayList<>();
        hauledItems = new ArrayList<>();
        desiredSlots = new ArrayList<>();
    }

    /**
     * For hauling item.
     * Validity should be fully checked by name (slots should be free).
     */
    public void pickupItem(Item item) {
        for (GrabEquipmentSlot slot : grabSlots.values()) {
            if (slot.grabbedItem != null) continue;
            slot.grabbedItem = item;
            hauledItems.add(item);
            return;
        }
        //TODO haul in containers
    }

    /**
     * Equips wear on body and tools into hands.
     * Validity should be fully checked by name (slots should be free).
     *
     * @return false, if equipping failed.
     */
    public boolean equipItem(Item item) {
        //TODO checkItems hauling
        if (item == null || equippedItems.contains(item)) return false;
        if (item.isWear()) { // equip as wear
            //TODO add layers checking
            List<EquipmentSlot> slots = selectMostEmptySlotsForItem(item);
            for (EquipmentSlot slot : slots) {
                slot.items.add(item);
            }
            equippedItems.add(item);
            return true;
        } else if (item.isTool()) { // grab as tool
            for (GrabEquipmentSlot slot : grabSlots.values()) {
                if (slot.grabbedItem != null) continue;
                slot.grabbedItem = item;
                equippedItems.add(item);
                return true;
            }
        }
        return false;
    }

    /**
     * Picks most unfilled slots with specified limb types.
     *
     * @return list of found slots otherwise.
     */
    private List<EquipmentSlot> selectMostEmptySlotsForItem(Item item) {
        if (item.isWear()) {
            List<EquipmentSlot> slots = new ArrayList<>(this.slots.values());
            List<EquipmentSlot> selectedSlots = new ArrayList<>();
            for (String type : item.getType().wear.getAllBodyParts()) {
                EquipmentSlot slot = selectMostSuitableSlotWithType(slots, type, item.getType().wear.getLayer());
                if (slot == null) continue;
                slots.remove(slot);
                selectedSlots.add(slot);
            }
            return selectedSlots;
        } else if (item.isTool()) {
            List<EquipmentSlot> slots = new ArrayList<>();
            for (GrabEquipmentSlot slot : grabSlots.values()) {
                if (slot.grabbedItem != null) continue;
                slots.add(slot);
                break;
            }
            slots.add(grabSlots.values().iterator().next());
            return slots;
        }
        return new ArrayList<>();
    }

    /**
     * Selects slot most suitable for equipping item of given layer.
     */
    private EquipmentSlot selectMostSuitableSlotWithType(List<EquipmentSlot> slots, String type, int layer) {
        //TODO take in account insulation and other properties
        EquipmentSlot emptyestSlot = null;
        for (EquipmentSlot slot : slots) {
            if (!slot.limbType.equals(type)) continue;
            if ((emptyestSlot == null ||
                    (emptyestSlot.isLayerOccupied(layer) && !slot.isLayerOccupied(layer)) ||
                    (emptyestSlot.getItemCountAboveLayer(layer) > slot.getItemCountAboveLayer(layer)) ||
                    (emptyestSlot.getTopLayer() > slot.getTopLayer()))) {
                emptyestSlot = slot;
            }
        }
        return emptyestSlot;
    }

    /**
     * Checks if given item can be equipped to creature.
     * Checks required and optional slots, demanded by item.
     *
     * @return Item that should be unequipped. nullm if all needed slots are free enough.
     * @throws NotSuitableItemException if ite cannot be equipped.
     */
    public Item checkItemForEquip(Item item) throws NotSuitableItemException {
        if (item.isWear()) {
            //slots with this limb types should exist. types in the list nto unique.
            ArrayList<String> requiredSlotTypes = item.getType().wear.getRequiredBodyParts();
            if (!checkSlotsWithTypes(requiredSlotTypes)) { // required slots exist on creature
                throw new NotSuitableItemException("Creature " + entity + " has no required slots for item " + item);
            }
            List<EquipmentSlot> slots = selectMostEmptySlotsForItem(item);
            for (EquipmentSlot slot : slots) {
                Item itemToUnequip = findItemToUnequip(slot, item);
                if (itemToUnequip != null) return itemToUnequip;
            }
            return null;
        } else if (item.isTool()) {
            List<EquipmentSlot> slots = selectMostEmptySlotsForItem(item);
            if (slots.size() > 0) return ((GrabEquipmentSlot) slots.get(0)).grabbedItem;
            throw new NotSuitableItemException("No slots for tool found");
        }
        throw new NotSuitableItemException("Item " + item + " cannot be equipped.");
    }

    /**
     * Checks if for each type from given list exists an least one slot with this type.
     */
    private boolean checkSlotsWithTypes(List<String> limbTypes) {
        limbTypes = new ArrayList<>(limbTypes);
        for (EquipmentSlot slot : slots.values()) {
            limbTypes.remove(slot.limbType);
            if (limbTypes.isEmpty()) break;
        }
        return limbTypes.isEmpty();
    }

    public boolean toolWithActionEquipped(String action) {
        return equippedItems.stream().anyMatch(item ->
                item.getType().tool.getActions().stream().anyMatch(toolAction ->
                        toolAction.action.equals(action)));
    }

    /**
     * Returns one of item from given slot that should be unequipped to equip given item.
     */
    private Item findItemToUnequip(EquipmentSlot slot, Item item) {
        for (int i = slot.items.size() - 1; i >= 0; i--) {
            if (slot.items.get(i).getType().wear.getLayer() <= item.getType().wear.getLayer()) continue;
            slot.items.get(i); // if name possible
        }
        return null; //name not required
    }

    /**
     * Filters and returns slots needed to be filled to avoid nudity penalty.
     */
    public List<EquipmentSlot> getEmptyDesiredSlots() {
        if (emptyDesiredSlotsCount == 0) return Collections.EMPTY_LIST;
        return desiredSlots.stream().filter(equipmentSlot -> equipmentSlot.isEmpty()).collect(Collectors.toList());
    }


    /**
     * Removes given item from all slots disregarding other item in these slots (even if overlapping is present).
     * Item should not be blocked by other item. This should be checked by name.
     */
    public void unequipItem(Item item) {
        if (!equippedItems.contains(item)) return;
        equippedItems.remove(item);
        slots.forEach((s, slot) -> {
            if (slot.items.contains(item)) slot.items.remove(item);
        });
    }

    /**
     * Removes given item from all grab slots.
     */
    public void dropItem(Item item) {
        if (!hauledItems.contains(item)) return;
        hauledItems.remove(item);
        grabSlots.forEach((s, slot) -> {
            if (slot.grabbedItem == item) slot.grabbedItem = null;
        });
    }

    public boolean checkItem(Item item) {
        return equippedItems.contains(item) || hauledItems.contains(item);
    }

    public ArrayList<Item> getEquippedItems() {
        return equippedItems;
    }

    public ArrayList<Item> getHauledItems() {
        return hauledItems;
    }

    public HashMap<String, EquipmentSlot> getSlots() {
        return slots;
    }

    public HashMap<String, GrabEquipmentSlot> getGrabSlots() {
        return grabSlots;
    }

    public ArrayList<EquipmentSlot> getDesiredSlots() {
        return desiredSlots;
    }

    public int getEmptyDesiredSlotsCount() {
        return emptyDesiredSlotsCount;
    }

    public void setEmptyDesiredSlotsCount(int emptyDesiredSlotsCount) {
        this.emptyDesiredSlotsCount = emptyDesiredSlotsCount;
    }
}