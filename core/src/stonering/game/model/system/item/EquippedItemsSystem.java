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
        // update equipped items
    }

    public void itemEquipped(Item item, EquipmentAspect aspect) {
        if(container.contained.containsKey(item)) Logger.ITEMS.logError("Adding to unit item not removed from container");
        if(item.position != null) Logger.ITEMS.logError("Adding to unit item not removed from map");
        item.position = null;
        container.equipped.put(item, aspect);
    }

    public void itemUnequipped(Item item) {
        if (container.equipped.remove(item) == null)
            Logger.ITEMS.logWarn("Items inconsistency: item " + item + " is not registered in ItemContainer as equipped");
    }

    public void removeItemFromEquipment(Item item) {
        if(!container.equipped.get(item).unequipItem(item))
            Logger.ITEMS.logWarn("Items inconsistency: item " + item + " is not stored in container aspect");
        if (container.contained.remove(item) == null)
            Logger.ITEMS.logWarn("Items inconsistency: item " + item + " is not registered in ItemContainer as contained");
    }

    public boolean isItemEquipped(Item item) {
        return container.equipped.containsKey(item);
    }
}
