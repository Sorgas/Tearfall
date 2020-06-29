package stonering.game.model.system.item;

import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.EntitySystem;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.logging.Logger;

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
        validateBeforeAdding(item);
        item.position = null;
        container.equipped.put(item, aspect);
    }

    public void itemUnequipped(Item item) {
        validateBeforeRemoving(item);
        container.equipped.remove(item);
    }

    public void removeItemFromEquipment(Item item) {
        EquipmentAspect aspect = container.equipped.get(item);
        if(aspect != null) {
            GameMvc.model().get(UnitContainer.class).equipmentSystem.removeItem(aspect, item);
            itemUnequipped(item);
        } else {
            Logger.ITEMS.logWarn("Items inconsistency: item " + item + " is not stored in equipment aspect");
        }
    }

    public boolean isItemEquipped(Item item) {
        return container.equipped.containsKey(item);
    }

    private void validateBeforeAdding(Item item) {
        validateOther(item);
        if(container.isItemEquipped(item)) Logger.ITEMS.logError("Items inconsistency: item " + item + " is equipped");
    }

    private void validateBeforeRemoving(Item item) {
        validateOther(item);
        if(!container.isItemEquipped(item)) Logger.ITEMS.logError("Items inconsistency: item " + item + " is not equipped");
    }

    private void validateOther(Item item) {
        if(container.isItemOnMap(item)) Logger.ITEMS.logError("Items inconsistency: item " + item + " is on map");
        if(container.isItemInContainer(item)) Logger.ITEMS.logError("Items inconsistency: item " + item + " is in container");
    }
}
