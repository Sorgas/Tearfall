package stonering.entity.unit.aspects.equipment;

import stonering.entity.Entity;
import stonering.entity.item.aspects.WearAspect;
import stonering.entity.job.action.Action;
import stonering.entity.Aspect;
import stonering.entity.item.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Stores all item equipped and hauled by unit.
 * Equipped item are ones in slots. Hauled items are ones in the grab slots and worn containers.
 * Equipping and unequipping can require additional actions, this logic should be implemented in {@link Action}s.
 * Does not takes or puts item to map, this should be done by {@link Action}s.
 * TODO add ears and fingers.
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
     * For hauling items in hands. Validity should be fully checked by action (slots should be free).
     * //TODO add hauling of large items in two hands.
     */
    public boolean pickupItem(Item item) {
        //TODO haul in containers
        Optional<GrabEquipmentSlot> optional = grabSlots.values().stream().filter(slot -> slot.grabbedItem == null).findFirst();
        if(!optional.isPresent()) return false; // no free slot found
        optional.get().grabbedItem = item;
        hauledItems.add(item);
        return true;
    }

    /**
     * Equips wear on body and tools into hands.
     * Validity should be fully checked by action (slots should be free), otherwise action will fail.
     *
     * @return false, if equipping failed.
     */
    public boolean equipItem(Item item) {
        //TODO check hauling
        if (item == null || equippedItems.contains(item)) return false;
        EquipmentSlot slot = getSlotForItem(item);
        if (slot == null || !slot.addItem(item)) return false; // slot is full
        equippedItems.add(item);
        return true;
    }

    /**
     * Removes given item from slot disregarding other item in this slot (even if overlapping is present).
     * Item should not be blocked by other items. This should be checked by action.
     */
    public boolean unequipItem(Item item) {
        EquipmentSlot slot = getSlotWithItem(item);
        if (slot == null) return false;
        equippedItems.remove(item);
        slot.removeItem(item);
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
