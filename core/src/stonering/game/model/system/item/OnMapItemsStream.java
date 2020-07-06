package stonering.game.model.system.item;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.job.action.target.ActionTarget;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.items.ItemTagEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.EntityStream;
import stonering.util.geometry.Position;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Wrapping for {@link Stream} to provide convenient methods for selecting items.
 *
 * @author Alexander on 14.10.2019.
 */
public class OnMapItemsStream extends EntityStream<Item> {
    private final ItemContainer container;

    public OnMapItemsStream(List<Item> entities) {
        super(entities);
        container = container();
    }

    public OnMapItemsStream() {
        container = container();
        stream = container().itemMap.values().stream().flatMap(Collection::stream);
    }

    public OnMapItemsStream filterHasTag(ItemTagEnum tag) {
        if(tag != null) stream = stream.filter(item -> item.tags.contains(tag));
        return this;
    }

    public OnMapItemsStream filterHasTag(String tag) {
        return filterHasTag(ItemTagEnum.get(tag));
    }

    public OnMapItemsStream filterNoTag(ItemTagEnum tag) {
        if(tag != null) stream = stream.filter(item -> !item.tags.contains(tag));
        return this;
    }

    public OnMapItemsStream filterByType(String type) {
        stream = stream.filter(item -> item.type.name.equals(type));
        return this;
    }

    public OnMapItemsStream filterByTypes(List<String> types) {
        stream = stream.filter(item -> types.contains(item.type.name));
        return this;
    }

    public OnMapItemsStream filterEquipped() {
        stream = stream.filter(container.equippedItemsSystem::isItemEquipped);
        return this;
    }

    public OnMapItemsStream filterBySelector(ItemSelector selector) {
        stream = stream.filter(selector::checkItem);
        return this;
    }

    public <T extends Aspect> OnMapItemsStream filterHasAspect(Class<T> type) {
        stream = stream.filter(item -> item.has(type));
        return this;
    }

    @Override
    public OnMapItemsStream filterByReachability(Position position) {
        //TODO add contained items
        return (OnMapItemsStream) super.filterByReachability(position);
    }

    public OnMapItemsStream filterByReachability(ActionTarget target) {
        return (OnMapItemsStream) super.filterByReachability(target.getPosition(), target.type != ActionTargetTypeEnum.EXACT);
    }

    @Override
    public Item getNearestTo(Position position) {
        return super.getNearestTo(position);
    }

    @Override
    public OnMapItemsStream getNearestTo(Position position, int number) {
        return (OnMapItemsStream) super.getNearestTo(position, number);
    }

    @Override
    public OnMapItemsStream filterOnMap() {
        return (OnMapItemsStream) super.filterOnMap();
    }

    @Override
    public OnMapItemsStream sorted(Comparator<Entity> comparator) {
        return (OnMapItemsStream) super.sorted(comparator);
    }

    @Override
    public OnMapItemsStream filter(Predicate<? super Item> predicate) {
        return (OnMapItemsStream) super.filter(predicate);
    }

    @Override
    public List<Item> toList() {
        return super.toList();
    }

    @Override
    public OnMapItemsStream filterNotInList(List<Item> list) {
        return (OnMapItemsStream) super.filterNotInList(list);
    }

    @Override
    protected ItemContainer container() {
        return container != null ? container : GameMvc.model().get(ItemContainer.class);
    }
}
