package stonering.game.model.system.item;

import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.EntitySystem;
import stonering.util.global.Logger;

/**
 * System for updating items equipped on units.
 * Methods should be called in pairs with other system methods.
 * TODO Items stored in worn containers
 *
 * @author Alexander on 17.11.2019.
 */
public class EquippedItemsSystem extends EntitySystem<Item> {
    private ItemContainer container;

    public EquippedItemsSystem(ItemContainer container) {
        this.container = container;
        updateInterval = TimeUnitEnum.MINUTE;
    }

    @Override
    public void update(Item entity) {

    }

    public void itemEquipped(Item item, EquipmentAspect aspect) {
        if(container.contained.keySet().contains(item)) Logger.ITEMS.logError("Adding to unit item not removed from wb");
        if(item.position != null) Logger.ITEMS.logError("Adding to unit item not removed from map");
        item.position = null;
        container.equipped.put(item, aspect);
    }

    public void itemUnequipped(Item item) {
        if (container.equipped.remove(item) == null)
            Logger.ITEMS.logWarn("Items inconsistency: item " + item + " is not registered in ItemContainer as equipped");
    }

    public boolean isItemEquipped(Item item) {
        return container.equipped.containsKey(item);
    }
}
