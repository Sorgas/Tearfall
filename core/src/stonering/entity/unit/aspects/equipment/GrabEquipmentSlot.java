package stonering.entity.unit.aspects.equipment;

import stonering.entity.item.Item;

import java.util.List;

/**
 * Extends regular {@link EquipmentSlot} with item that can be held.
 *
 * @author Alexander on 22.02.2019.
 */
public class GrabEquipmentSlot extends EquipmentSlot {
    public Item grabbedItem; // null, if free

    public GrabEquipmentSlot(String name, List<String> limbs, EquipmentAspect aspect) {
        super(name, limbs, aspect);
    }

    /**
     * Tool item are also suitable for this slot
     */
    @Override
    public boolean isSuitableFor(Item item) {
        return super.isSuitableFor(item) || item.type.tool != null;
    }

    @Override
    public boolean canUnequip(Item item) {
        return grabbedItem == item || super.canUnequip(item);
    }
}