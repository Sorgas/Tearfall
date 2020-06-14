package stonering.game.model.system.item;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.EntitySystem;
import stonering.util.global.Logger;

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
        // TODO update items in containers
    }

    public void addItemToContainer(Item item, ItemContainerAspect aspect) {
        if(container.equipped.containsKey(item)) Logger.ITEMS.logError("Adding to container item not removed from unit");
        if(container.contained.containsKey(item)) Logger.ITEMS.logError("Adding to container item not removed from another item container");
        aspect.items.add(item); // put to container
        container.contained.put(item, aspect); // register as contained
        item.position = aspect.entity.position; // set item position
    }

    public void removeItemFromContainer(Item item) {
        if(!container.contained.get(item).items.remove(item)) Logger.ITEMS.logWarn("Items inconsistency: item " + item + " is not stored in container aspect");
        if (container.contained.remove(item) == null) Logger.ITEMS.logWarn("Items inconsistency: item " + item + " is not registered in ItemContainer as contained");
        item.position = null; // clear item position
    }

    public boolean isItemContained(Item item) {
        return container.contained.containsKey(item);
    }
}
