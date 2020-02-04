package stonering.game.model.system.item;

import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.enums.items.ItemTagEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.EntityStream;

import java.util.List;
import java.util.stream.Stream;

/**
 * Wrapping for {@link Stream} to provide convenient methods for selecting items.
 *
 * @author Alexander on 14.10.2019.
 */
public class ItemsStream extends EntityStream<Item> {
    private ItemContainer container = getContainer();

    public ItemsStream(List<Item> entities) {
        super(entities);
    }

    public ItemsStream() {
        super();
    }

    public ItemsStream filterHasTag(ItemTagEnum tag) {
        stream = stream.filter(item -> item.tags.contains(tag));
        return this;
    }

    public ItemsStream filterHasTag(String tag) {
        return filterHasTag(ItemTagEnum.get(tag));
    }

    public ItemsStream filterNoTag(ItemTagEnum tag) {
        stream = stream.filter(item -> !item.tags.contains(tag));
        return this;
    }

    public ItemsStream filterByType(String type) {
        stream = stream.filter(item -> item.getType().name.equals(type));
        return this;
    }

    public ItemsStream filterByTypes(List<String> types) {
        stream = stream.filter(item -> types.contains(item.getType().name));
        return this;
    }

    public ItemsStream filterContained() {
        stream = stream.filter(container.containedItemsSystem::itemIsContained);
        return this;
    }

    public ItemsStream filterEquipped() {
        stream = stream.filter(container.equippedItemsSystem::isItemEquipped);
        return this;
    }

    public ItemsStream filterBySelector(ItemSelector selector) {
        stream = stream.filter(selector::checkItem);
        return this;
    }

    @Override
    protected ItemContainer getContainer() {
        return GameMvc.model().get(ItemContainer.class);
    }
}
