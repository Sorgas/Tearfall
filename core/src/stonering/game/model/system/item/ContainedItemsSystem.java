package stonering.game.model.system.item;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.EntitySystem;
import stonering.util.logging.Logger;

/**
 * System for updating items stored in workbenches and containers.
 * Methods should be called in pairs with other item util methods.
 *
 * @author Alexander on 17.11.2019.
 */
public class ContainedItemsSystem extends EntitySystem<Item> {
    private ItemContainer container;

    public ContainedItemsSystem(ItemContainer container) {
        this.container = container;
        updateInterval = TimeUnitEnum.MINUTE;
    }

    @Override
    public void update(Item entity) {
        // update items in containers
    }

    public void addItemToContainer(Item item, ItemContainerAspect aspect) {
        if(container.equipped.containsKey(item)) Logger.ITEMS.logError("Adding to container item not removed from unit");
        if(container.contained.containsKey(item)) Logger.ITEMS.logError("Adding to container item not removed from another item container");
        aspect.items.add(item); // put to container
        container.contained.put(item, aspect); // register as contained
        item.position = aspect.entity.position; // set item position
    }

    public void removeItemFromContainer(Item item) {
        validateForRemoving(item);
        container.contained.get(item).items.remove(item);
        item.position = null; // clear item position
    }

    private void validateForAdding(Item item) {
        validateOther(item);
        if(container.isItemInContainer(item)) Logger.ITEMS.logError("Items inconsistency: item " + item + " is already contained");
    }

    private void validateForRemoving(Item item) {
        validateOther(item);
        if(!container.isItemInContainer(item)) Logger.ITEMS.logError("Items inconsistency: item " + item + " is not contained");
        if(!container.contained.get(item).items.contains(item)) Logger.ITEMS.logError("Items inconsistency: item " + item + " is not in registered container");
    }

    private void validateOther(Item item) {
        if(container.isItemEquipped(item)) Logger.ITEMS.logError("Items inconsistency: item " + item + " is equipped");
        if(container.isItemOnMap(item)) Logger.ITEMS.logError("Items inconsistency: item " + item + " is on map");
    }
}
