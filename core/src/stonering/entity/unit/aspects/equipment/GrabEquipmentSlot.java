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

    public GrabEquipmentSlot(String name, List<String> limbs) {
        super(name, limbs);
    }

    @Override
    public boolean hasItem(Item item) {
        return super.hasItem(item) || grabbedItem == item;
    }

    @Override
    public boolean addItem(Item item) {
        if(super.addItem(item)) return true; // try to equip like wear
        if (isEquippableTool(item)) { // equip as tool
            grabbedItem = item;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeItem(Item item) {
        if(super.removeItem(item)) return true; // try to unequip like wear
        if(grabbedItem == item) {
            grabbedItem = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean isSuitableFor(Item item) {
        return isEquippableTool(item) || super.isSuitableFor(item);
    }

    private boolean isEquippableTool(Item item) {
        return grabbedItem == null && item != null && item.isTool();
    }

    @Override
    public boolean canUnequip(Item item) {
        return grabbedItem == item || super.canUnequip(item);
    }

    @Override
    public Item getBlockingItem(Item item) {
        return super.getBlockingItem(item);
    }
}