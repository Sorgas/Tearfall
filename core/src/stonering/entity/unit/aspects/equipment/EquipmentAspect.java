package stonering.entity.unit.aspects.equipment;

import stonering.entity.Entity;
import stonering.entity.Aspect;
import stonering.entity.item.Item;
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
    public Item itemBuffer;
    
    public EquipmentAspect(Entity entity) {
        super(entity);
        slots = new HashMap<>();
        grabSlots = new HashMap<>();
        items = new HashSet<>();
        desiredSlots = new ArrayList<>();
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
        return slotStream().filter(slot -> slot.item == item).findFirst();
    }
    
    public Optional<GrabEquipmentSlot> getGrabSlotWithItem(Item item) {
        return grabSlotStream().filter(slot -> slot.grabbedItem == item).findFirst();
    }
    
    public Stream<EquipmentSlot> slotStream() {
        return slots.values().stream();
    }
    
    public Stream<GrabEquipmentSlot> grabSlotStream() {
        return grabSlots.values().stream();
    }
}
