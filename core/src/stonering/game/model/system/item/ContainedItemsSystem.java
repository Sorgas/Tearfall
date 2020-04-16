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
        // update items in containers
    }

    public void addItemToContainer(Item item, ItemContainerAspect aspect) {
        if(container.equipped.containsKey(item)) Logger.ITEMS.logError("Adding to container item not removed from unit");
        if(item.position != null) Logger.ITEMS.logError("Adding to wb item not removed from map");
        aspect.items.add(item);
        container.contained.put(item, aspect);
    }

    public void removeItemFromContainer(Item item, ItemContainerAspect aspect) {
        if(!aspect.items.remove(item))
            Logger.ITEMS.logWarn("Items inconsistency: item " + item + " is not stored in container aspect");
        if (container.contained.remove(item) == null)
            Logger.ITEMS.logWarn("Items inconsistency: item " + item + " is not registered in ItemContainer as contained");
    }

    public boolean isItemContained(Item item) {
        return container.contained.containsKey(item);
    }
}
