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
    public final EquipmentAspect aspect;
    public final String name;
    public Item item; //TODO mvp single item, add layers
    public final List<String> limbs; // limbs covered by items in this slot. items can cover additional limbs
    
    public EquipmentSlot(String name, List<String> limbs, EquipmentAspect aspect) {
        this.name = name;
        this.limbs = new ArrayList<>(limbs);
        this.aspect = aspect;
    }
}
