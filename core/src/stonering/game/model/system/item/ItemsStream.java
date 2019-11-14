package stonering.game.model.system.item;

import stonering.entity.item.Item;
import stonering.enums.items.TagEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Wrapping for {@link Stream} to provide more specific methods.
 *
 * @author Alexander on 14.10.2019.
 */
public class ItemsStream {
    public Stream<Item> stream;

    public ItemsStream(Stream<Item> stream) {
        this.stream = stream;
    }

    public ItemsStream(Collection<Item> items) {
        stream = items.stream();
    }

    public ItemsStream filterByTag(TagEnum tag) {
        stream = stream.filter(item -> item.tags.contains(tag));
        return this;
    }

    public ItemsStream filterByTag(String tag) {
        return filterByTag(TagEnum.get(tag));
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
        stream = stream.filter(item -> GameMvc.instance().getModel().get(LocalMap.class).passage.util.positionReachable(position, item.position, false));
        return this;
    }

    public Item getNearestTo(Position position) {
        return stream.min((item1, item2) -> Math.round(item1.position.getDistance(position))).orElse(null);
    }

    public ItemsStream getNearestTo(Position position, int number) {
        stream = stream.sorted(Comparator.comparingInt(item -> position.fastDistance(item.position))).limit(number);
        return this;
    }

    public List<Item> toList() {
        return stream.collect(Collectors.toList());
    }
}
