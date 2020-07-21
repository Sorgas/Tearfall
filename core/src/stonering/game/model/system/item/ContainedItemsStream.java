package stonering.game.model.system.item;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.job.action.target.ActionTarget;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Wrapped stream for searching items in containers.
 *
 * @author Alexander on 12.05.2020.
 */
public class ContainedItemsStream {
    private final ItemContainer container;
    public Stream<Map.Entry<Item, ItemContainerAspect>> stream;
    
    public ContainedItemsStream() {
        container = GameMvc.model().get(ItemContainer.class);
        stream = container.contained.entrySet().stream();
    }

    public ContainedItemsStream filterLockedContainers() {
        return this;
    }

    public ContainedItemsStream filterBySelector(ItemSelector selector) {
        stream = stream.filter(entry -> selector.checkItem(entry.getKey()));
        return this;
    }

    public ContainedItemsStream filterOwnedContainers() {
        return this;
    }

    public ContainedItemsStream filterByReachability(Position position) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        stream = stream.filter(entry -> map.passageMap.util.positionReachable(position, entry.getValue().entity.position, true));
        return this;
    }

    public ContainedItemsStream filterByReachability(ActionTarget target) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        stream = stream.filter(entry -> map.passageMap.util.positionReachable(target.getPosition(), entry.getValue().entity.position, target.type != ActionTargetTypeEnum.EXACT));
        return this;
    }

    public ContainedItemsStream filterNotInList(List<Item> list) {
        stream = stream.filter(entry -> !list.contains(entry.getKey()));
        return this;
    }

    public List<Item> toList() {
        return stream.map(Map.Entry::getKey).collect(Collectors.toList());
    }
}
