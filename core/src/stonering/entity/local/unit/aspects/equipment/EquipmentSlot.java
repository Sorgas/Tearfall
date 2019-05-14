package stonering.entity.local.unit.aspects.equipment;

import stonering.entity.local.items.Item;

import java.util.ArrayList;

/**
 * @author Alexander on 22.02.2019.
 */
public class EquipmentSlot {
    //TODO add multi-item support for one layer
    public ArrayList<Item> items; //lower indexes means item is under other items.
    public String limbName;
    public String limbType;

    public EquipmentSlot(String limbName, String limbType) {
        this.limbName = limbName;
        this.limbType = limbType;
        items = new ArrayList<>();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getTopLayer() {
        return !items.isEmpty() ? items.get(items.size() - 1).getType().wear.getLayer() : 0;
    }

    public boolean isLayerOccupied(int layer) {
        return items.stream().anyMatch(item -> item.getType().wear.getLayer() == layer);
    }

    public int getItemCountAboveLayer(int layer) {
        return (int) items.stream().filter(item -> item.getType().wear.getLayer() > layer).count();
    }
}
