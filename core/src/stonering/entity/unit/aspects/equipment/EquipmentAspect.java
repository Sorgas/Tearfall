package stonering.entity.unit.aspects.equipment;

import stonering.entity.Entity;
import stonering.entity.Aspect;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.WearAspect;
import stonering.stage.entity_menu.unit.UnitEquipmentTab;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stores all item equipped and hauled by unit. See {@link stonering.game.model.system.unit.CreatureEquipmentSystem}.
 * TODO add all slots mentioned in {@link UnitEquipmentTab}.
 *
 * @author Alexander Kuzyakov on 03.01.2018.
 */
public class EquipmentAspect extends Aspect {
    public final HashMap<String, EquipmentSlot> slots;            // all slots of a creature (for wear)
    public final HashMap<String, GrabEquipmentSlot> grabSlots;    // slots for tools (subset of all slots)
    public final Set<Item> items;                           // items in worn containers and in hands
    public final List<EquipmentSlot> desiredSlots;                // uncovered limbs give comfort penalty

    public EquipmentAspect(Entity entity) {
        super(entity);
        slots = new HashMap<>();
        grabSlots = new HashMap<>();
        items = new HashSet<>();
        desiredSlots = new ArrayList<>();
    }

    /**
     * Sets given item to appropriate slot. all blocking items are unset from slot.
     *
     * @param item item to set. Should have {@link WearAspect}
     * @return list of items that were blocking given item.
     */
    public List<Item> equipItem(Item item) {
        return Optional.ofNullable(item.get(WearAspect.class))
                .map(wear -> slots.get(wear.slot))
                .map(slot -> {
                    Item previousItem = slot.item; // TODO collect from layers here
                    slot.item = item;
                    return Arrays.asList(previousItem);
                })
                .orElse(Collections.emptyList());
    }

    /**
     * Removes given item from slot disregarding other item in this slot (even if overlapping is present).
     * Item should not be blocked by other items. This should be checked by action.
     */
    public Item unequipItem(Item item) {
        return getSlotWithItem(item)
                .map(slot -> {
                    Item qwer = slot.item;
                    slot.item = null;
                    return qwer;
                })
                .orElse(null);
    }

    public List<Item> takeItem(Item item) {
        List<Item> replacedItems = new ArrayList<>();
        grabSlots.values().stream()
                .min((slot1, slot2) -> Boolean.compare(slot1.grabbedItem == null, slot2.grabbedItem == null)) // select empty slots first
                .ifPresent(slot -> {
                    replacedItems.add(slot.item);
                    items.remove(slot.item);
                    items.add(slot.item = item);
                });
        return replacedItems;
    }

    /**
     * Removes given item from all grab slots. TODO handle containers, like backpacks
     */
    public Item dropItem(Item item) {
        return grabSlots.values().stream()
                .filter(slot -> slot.grabbedItem == item)
                .map(slot -> {
                    slot.grabbedItem = null;
                    items.remove(item);
                    return item;
                })
                .findFirst()
                .orElse(null);
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
    public Stream<EquipmentSlot> getEmptyDesiredSlots() {
        return desiredSlots.stream().filter(equipmentSlot -> equipmentSlot.item == null);
    }

    public boolean removeItem(Item item) {
        EquipmentSlot itemSlot = getSlotWithItem(item).orElse(null);
        if (itemSlot != null) {
            itemSlot.item = null;
            return true;
        }
        GrabEquipmentSlot grabItemSlot = grabSlots.values().stream()
                .filter(slot -> slot.grabbedItem == item)
                .findFirst()
                .orElse(null);
        if (grabItemSlot != null) {
            grabItemSlot.grabbedItem = null;
            return true;
        }
        return false;
    }

    public boolean toolWithActionEquipped(String action) {
        return items.stream()
                .map(item -> item.type.tool)
                .filter(Objects::nonNull)
                .flatMap(toolType -> toolType.getActions().stream())
                .anyMatch(toolAction -> Objects.equals(toolAction.action, action));
    }

    public List<Item> getEquippedTools() {
        return items.stream().filter(item -> item.type.tool != null).collect(Collectors.toList());
    }

    public Optional<EquipmentSlot> getSlotWithItem(Item item) {
        return slots.values().stream()
                .filter(slot -> slot.item == item)
                .findFirst();
    }
    
    public Stream<EquipmentSlot> slotStream() {
        return slots.values().stream();
    }
    
    public Stream<GrabEquipmentSlot> grabSlotStream() {
        return grabSlots.values().stream();
    }
}
