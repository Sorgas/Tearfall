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
    public final String name;
    public Item item; //TODO mvp single item, add layers
    public final List<String> limbs; // limbs covered by items in this slot. items can cover additional limbs

    public EquipmentSlot(String name, List<String> limbs) {
        this.name = name;
        this.limbs = new ArrayList<>(limbs);
    }

    /**
     * Checks that item is appropriate for slot. Does not check slot status. 
     */
    public boolean isSuitableFor(Item item) {
        return item != null
                && item.getOptional(WearAspect.class).map(wear -> wear.slot.equals(name)).orElse(false);
    }

    /**
     * Checks that item can be equipped. 
     */
    public boolean canEquip(Item item) {
        return this.item == null;
    }
    
    /**
     * Item is equipped in this slot.
     * TODO mvp
     */
    public boolean canUnequip(Item item) {
        return item == this.item;
    }
}
