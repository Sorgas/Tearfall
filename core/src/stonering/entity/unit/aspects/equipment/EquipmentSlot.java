package stonering.entity.unit.aspects.equipment;

import stonering.entity.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Slots are stored in creature's {@link EquipmentAspect} and can hold items.
 *
 * @author Alexander on 22.02.2019.
 */
public class EquipmentSlot {
    //TODO add multi-item support for one layer
    public final Item[] items; // single item of each layer
    public final String name;
    public final List<String> limbs;

    public EquipmentSlot(String name, List<String> limbs) {
        this.name = name;
        this.limbs = new ArrayList<>(limbs);
        items = new Item[5];
    }

//    public boolean isEmpty() {
//        return items.isEmpty();
//    }
//
//    public int getTopLayer() {
//        return !items.isEmpty() ? items.get(items.size() - 1).getType().wear.getLayer() : 0;
//    }
//
//    public boolean isLayerOccupied(int layer) {
//        return items.stream().anyMatch(item -> item.getType().wear.getLayer() == layer);
//    }
//
//    public int getItemCountAboveLayer(int layer) {
//        return (int) items.stream().filter(item -> item.getType().wear.getLayer() > layer).count();
//    }
}
