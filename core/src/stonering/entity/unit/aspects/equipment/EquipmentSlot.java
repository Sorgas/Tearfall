package stonering.entity.unit.aspects.equipment;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.WearAspect;

import java.util.ArrayList;
import java.util.List;

/**
 * Slots are stored in creature's {@link EquipmentAspect} and can hold wear items.
 *
 * @author Alexander on 22.02.2019.
 */
public class EquipmentSlot {
    //TODO add multi-item support for one layer

    // public final Item[] items; // single item of each layer
    public Item item; //TODO mvp single item
    public final String name;
    public final List<String> limbs; // limbs covered by items in this slot. items can cover additional slots

    public EquipmentSlot(String name, List<String> limbs) {
        this.name = name;
        this.limbs = new ArrayList<>(limbs);
    }

    public boolean hasItem(Item item) {
        return this.item == item;
    }

    public boolean addItem(Item item) {
        if(isEquippable(item)) {
            this.item = item;
            return true;
        }
        return false;
    }

    public boolean removeItem(Item item) {
        if(this.canUnequip(item)) {
            this.item = null;
            return true;
        }
        return false;
    }

    public boolean canEquip(Item item) {
        return isEquippable(item);
    }

    /**
     * TODO mvp
     * Item is equippable wear and slot is free.
     */
    private boolean isEquippable(Item item) {
        return this.item == null && item != null && item.hasAspect(WearAspect.class);
    }

    /**
     * Item is equipped in this slot.
     * TODO mvp
     */
    public boolean canUnequip(Item item) {
        return hasItem(item);
    }
}
