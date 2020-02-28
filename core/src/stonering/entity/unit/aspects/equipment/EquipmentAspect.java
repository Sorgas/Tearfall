package stonering.entity.unit.aspects.equipment;

import stonering.entity.Entity;
import stonering.entity.Aspect;
import stonering.entity.item.Item;
import stonering.stage.unit.UnitEquipmentTab;
import stonering.util.global.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Stores all item equipped and hauled by unit. See {@link stonering.game.model.system.unit.CreatureEquipmentSystem}.
 * 
 * 
 * TODO add all slots mentioned in {@link UnitEquipmentTab}.
 * 
 * @author Alexander Kuzyakov on 03.01.2018.
 */
public class EquipmentAspect extends Aspect {
    public final HashMap<String, EquipmentSlot> slots;            // all slots of a creature (for wear)
    public final HashMap<String, GrabEquipmentSlot> grabSlots;    // slots for tools (subset of all slots)
//    public Item hauledItem;                                       // in-hand hauling item (mvp)
    
    public final List<Item> hauledItems;                     // hauled item list for faster checking (hauled in hands and worn containers)
    public final List<Item> equippedItems;                   // equipped item list for faster checking (worn items)
    public final List<EquipmentSlot> desiredSlots;           // uncovered limbs give comfort penalty

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
        Logger.UNITS.logDebug("Picking up item " + item);
        Optional<GrabEquipmentSlot> optional = grabSlots.values().stream().filter(slot -> slot.grabbedItem == null).findFirst();
        if (!optional.isPresent()) return false; // should have grab slot to equip new item
        optional.get().grabbedItem = item;
        hauledItems.add(item);
        Logger.UNITS.logDebug("");
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
//        slot.removeItem(item);
        return true;
    }
    
    public boolean toolWithActionEquipped(String action) {
        return equippedItems.stream()
                .map(item -> item.type.tool)
                .filter(Objects::nonNull)
                .flatMap(toolType -> toolType.getActions().stream())
                .anyMatch(toolAction -> Objects.equals(toolAction.action, action));
    }

    public List<Item> getEquippedTools() {
        return equippedItems.stream().filter(item -> item.type.tool != null).collect(Collectors.toList());
    }

    /**
     * Removes given item from all grab slots. TODO handle containers, like backpacks
     */
    public void dropItem(Item item) {
        if (!hauledItems.contains(item)) return;
        hauledItems.remove(item);
        grabSlots.forEach((s, slot) -> {
            if (slot.grabbedItem == item) slot.grabbedItem = null;
        });
    }

    /**
     * Current load / Max load [0,1].
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
        if (!equippedItems.contains(item)) return null; // item not equipped
        return slots.values().stream().filter(slot -> slot.item == item).findFirst().orElse(null); // find slot with item
    }

    public boolean isItemInGrabSlots(Item item) {
        return grabSlots.values().stream().anyMatch(slot -> slot.grabbedItem == item);
    }
}
