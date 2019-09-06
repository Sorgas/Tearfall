package stonering.entity.unit.aspects.equipment;

import stonering.entity.Entity;
import stonering.entity.job.action.Action;
import stonering.exceptions.NotSuitableItemException;
import stonering.entity.Aspect;
import stonering.entity.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stores all item equipped and hauled by unit.
 * Equipped item are ones in slots. Hauled items are ones in the grab slots.
 * Equipping and unequipping can require additional actions, this logic should be implemented in {@link Action}s.
 * Does not takes or puts item to map, this should be done by {@link Action}.
 * <p>
 * MVP: items have no layers, all slots mentioned in item type are occupied. Items can be hauled only in hands
 *
 * @author Alexander Kuzyakov on 03.01.2018.
 */
public class EquipmentAspect extends Aspect {
    public static String NAME = "equipment";
    public final HashMap<String, EquipmentSlot> slots;            // all slots of a creature
    public final HashMap<String, GrabEquipmentSlot> grabSlots;    // equipped item
    public final ArrayList<Item> hauledItems;                     // hauled item list for faster checking
    public final ArrayList<Item> equippedItems;                   // equipped item list for faster checking
    public final ArrayList<EquipmentSlot> desiredSlots;           // uncovered limbs give comfort penalty

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
     * Validity should be fully checked by action (slots should be free).
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
     * Validity should be fully prechecked by action (slots should be free), otherwise action will fail.
     *
     * @return false, if equipping failed.
     */
    public boolean equipItem(Item item) {
        //TODO check hauling
        if (item == null || equippedItems.contains(item)) return false;
     // grab as tool
        for (GrabEquipmentSlot slot : grabSlots.values()) {
            if (slot.grabbedItem != null) continue;
            slot.grabbedItem = item;
            equippedItems.add(item);
            return true;
        }
        return false;
    }

    public boolean equipWearItem(Item item) {
        if (item == null || !item.isWear() || equippedItems.contains(item)) return false;
        //TODO add layers checking
        List<EquipmentSlot> slots = selectMostEmptySlotsForItem(item);
        for (EquipmentSlot slot : slots) {
            slot.items.add(item);
        }
        equippedItems.add(item);
        return true;

    }

    public boolean equipToolItem(Item item) {
        if (item == null || equippedItems.contains(item)) return false;

    }

    /**
     * Picks most unfilled slots with specified limb types.
     *
     * @return list of found slots otherwise.
     */
    private List<EquipmentSlot> selectMostEmptySlotsForItem(Item item) {
        if (item.isWear()) {
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

    private List<EquipmentSlot> selectSlotsForWear(Item item) {
        List<EquipmentSlot> slots = new ArrayList<>(this.slots.values());
        List<EquipmentSlot> selectedSlots = new ArrayList<>();
        for (String type : item.getType().wear.getAllBodyParts()) {
            EquipmentSlot slot = selectMostSuitableSlotWithType(slots, type);
            if (slot == null) continue;
            slots.remove(slot);
            selectedSlots.add(slot);
        }
        return selectedSlots;

    }

    /**
     * Selects slot most suitable for equipping item.
     * //TODO add item layers
     */
    private EquipmentSlot selectMostSuitableSlotWithType(List<EquipmentSlot> slots, String type) {
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
     * @return Item that should be unequipped. null, if all needed slots are free enough.
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

    /**
     * Current load / Max load.
     */
    public float getRelativeLoad() {
        return 1; //TODO
    }
}
