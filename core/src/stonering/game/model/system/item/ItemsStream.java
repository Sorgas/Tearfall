package stonering.game.model.system.item;

import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.enums.items.ItemTagEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Wrapping for {@link Stream} to provide convenient methods for selecting items.
 *
 * @author Alexander on 14.10.2019.
 */
public class ItemsStream {
    private ItemContainer container = GameMvc.model().get(ItemContainer.class);
    public Stream<Item> stream;

    public ItemsStream(Collection<Item> items) {
        stream = items.stream();
    }

    public ItemsStream() {
        stream = container.entities.stream();
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

    public ItemsStream filterByReachability(Position position) {
        stream = stream.filter(item -> GameMvc.model().get(LocalMap.class).passageMap.util.positionReachable(position, item.position, false));
        return this;
    }

    public Item getNearestTo(Position position) {
        return stream.min(Comparator.comparingInt(item -> item.position.fastDistance(position))).orElse(null);
    }

    public ItemsStream getNearestTo(Position position, int number) {
        stream = stream.sorted(Comparator.comparingInt(item -> position.fastDistance(item.position))).limit(number);
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

    public ItemsStream filterOnMap() {
        stream = stream.filter(item -> item.position != null);
        return this;
    }

    public ItemsStream filterNotInList(List<Item> list) {
        stream = stream.filter(item -> !list.contains(item));
        return this;
    }

    public ItemsStream sorted(Comparator<Item> comparator) {
        stream = stream.sorted(comparator);
        return this;
    }

    public ItemsStream filter(Predicate<Item> predicate) {
        stream = stream.filter(predicate);
        return this;
    }

    public List<Item> toList() {
        return stream.collect(Collectors.toList());
    }
}
