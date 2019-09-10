package stonering.entity.unit.aspects.equipment;

import stonering.entity.Entity;
import stonering.entity.item.aspects.WearAspect;
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
     * For hauling items.
     *
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
     * Validity should be fully checked by action (slots should be free), otherwise action will fail.
     *
     * @return false, if equipping failed.
     */
    public boolean equipItem(Item item) {
        //TODO check hauling
        if (item == null || equippedItems.contains(item)) return false; // already equipped
        EquipmentSlot slot = getSlotForItem(item);
        if (slot == null || !slot.addItem(item)) return false; // slot is full
        equippedItems.add(item);
        return true;
    }

    /**
     * Returns slot for item to be equipped.
     * TODO select emptiest from appropriate slots.
     *
     * @return null if no slots available.
     */
    public EquipmentSlot getSlotForItem(Item item) {
        if (item.hasAspect(WearAspect.class) && slots.get(item.getAspect(WearAspect.class).slot) != null)
            return slots.get(item.getAspect(WearAspect.class).slot); // item is wear and slot exists
        if (item.isTool() && !grabSlots.isEmpty())
            return grabSlots.values().stream().filter(slot -> slot.canEquip(item)).findFirst().orElse(grabSlots.get(0)); // item is tool and grab slot exists.
        return null;
    }

    public boolean toolWithActionEquipped(String action) {
        return equippedItems.stream().anyMatch(item ->
                item.getType().tool.getActions().stream().anyMatch(toolAction ->
                        toolAction.action.equals(action)));
    }

    /**
     * Removes given item from slot disregarding other item in this slot (even if overlapping is present).
     * Item should not be blocked by other items. This should be checked by action.
     */
    public void unequipItem(Item item) {
        if (!equippedItems.contains(item)) return; // item not equipped
        if (!item.getType().hasAspect(WearAspect.class)) {
            equippedItems.remove(item);
            slots.forEach((s, slot) -> {
                if (slot.hasItem(item)) slot.item = null;
            });
        }
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

    /**
     * Filters and returns slots needed to be filled to avoid nudity penalty.
     */
    public List<EquipmentSlot> getEmptyDesiredSlots() {
        return desiredSlots.stream().filter(equipmentSlot -> equipmentSlot.item == null).collect(Collectors.toList());
    }

    public EquipmentSlot getSlotWithItem(Item item) {
        if(!equippedItems.contains(item)) return null; // item not equipped
        return slots.values().stream().filter(slot -> slot.hasItem(item)).findFirst().orElse(null); // find slot with item
    }
}
