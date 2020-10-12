package stonering.entity.unit.aspects.equipment;

import stonering.entity.Aspect;
import stonering.entity.item.Item;
import stonering.stage.entity_menu.unit.tab.UnitEquipmentTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Stores all item equipped and hauled by unit. See {@link stonering.game.model.system.unit.CreatureEquipmentSystem}.
 * TODO add all slots mentioned in {@link UnitEquipmentTab}.
 *
 * @author Alexander Kuzyakov on 03.01.2018.
 */
public class EquipmentAspect extends Aspect {
    public final HashMap<String, EquipmentSlot> slots = new HashMap<>();          // all slots of a creature (for wear)
    public final HashMap<String, GrabEquipmentSlot> grabSlots = new HashMap<>();  // slots for tools (subset of all slots)
    public final Set<Item> items = new HashSet<>();                               // items in worn containers and in hands
    public final List<EquipmentSlot> desiredSlots = new ArrayList<>();              // uncovered limbs give comfort penalty
    public Item hauledItem;

    /**
     * Filters and returns slots needed to be filled to avoid nudity penalty.
     */
    public Stream<EquipmentSlot> getEmptyDesiredSlots() {
        return desiredSlots.stream().filter(equipmentSlot -> equipmentSlot.item == null);
    }

    public boolean toolWithActionEquipped(String action) {
        return grabSlotStream()
                .filter(GrabEquipmentSlot::isToolGrabbed)
                .map(slot -> slot.grabbedItem.type.tool)
                .filter(Objects::nonNull)
                .flatMap(toolType -> toolType.actions.stream())
                .anyMatch(toolAction -> Objects.equals(toolAction.action, action));
    }

    public Optional<EquipmentSlot> slotWithItem(Item item) {
        return slotStream().filter(slot -> slot.item == item).findFirst();
    }
    
    public Optional<GrabEquipmentSlot> grabSlotWithItem(Item item) {
        return grabSlotStream().filter(slot -> slot.grabbedItem == item).findFirst();
    }
    
    public Stream<EquipmentSlot> slotStream() {
        return slots.values().stream();
    }
    
    public Stream<GrabEquipmentSlot> grabSlotStream() {
        return grabSlots.values().stream();
    }
}
