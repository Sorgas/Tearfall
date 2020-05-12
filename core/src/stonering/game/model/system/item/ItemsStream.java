package stonering.game.model.system.item;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.enums.items.ItemTagEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.EntityStream;
import stonering.util.geometry.Position;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Wrapping for {@link Stream} to provide convenient methods for selecting items.
 * TODO add contained items
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

    public ItemsStream filterEquipped() {
        stream = stream.filter(container.equippedItemsSystem::isItemEquipped);
        return this;
    }

    public ItemsStream filterBySelector(ItemSelector selector) {
        stream = stream.filter(selector::checkItem);
        return this;
    }

    @Override
    public ItemsStream filterByReachability(Position position) {
        //TODO add contained items
        return (ItemsStream) super.filterByReachability(position);
    }

    @Override
    public Item getNearestTo(Position position) {
        return super.getNearestTo(position);
    }

    @Override
    public ItemsStream getNearestTo(Position position, int number) {
        return (ItemsStream) super.getNearestTo(position, number);
    }

    @Override
    public ItemsStream filterOnMap() {
        return (ItemsStream) super.filterOnMap();
    }

    @Override
    public ItemsStream sorted(Comparator<Entity> comparator) {
        return (ItemsStream) super.sorted(comparator);
    }

    @Override
    public ItemsStream filter(Predicate<? super Item> predicate) {
        return (ItemsStream) super.filter(predicate);
    }

    @Override
    public List<Item> toList() {
        return super.toList();
    }

    @Override
    protected ItemContainer getContainer() {
        return GameMvc.model().get(ItemContainer.class);
    }

    @Override
    public ItemsStream filterNotInList(List<Item> list) {
        return (ItemsStream) super.filterNotInList(list);
    }
}
