package stonering.entity.local.unit.aspects.equipment;

import stonering.entity.local.item.Item;

/**
 * @author Alexander on 22.02.2019.
 */
public class GrabEquipmentSlot extends EquipmentSlot {
    public Item grabbedItem;

    public GrabEquipmentSlot(String limbName, String limbType) {
        super(limbName, limbType);
    }
}