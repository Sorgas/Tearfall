package stonering.game.model.system.item;

import java.util.Map;
import java.util.stream.Stream;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.item.selectors.ItemSelector;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Stream wrapping for searching items in containers.
 * 
 * @author Alexander on 12.05.2020.
 */
public class ContainedItemsStream {
    private Stream<Map.Entry<Item, ItemContainerAspect>> stream;
    
    public ContainedItemsStream() {
        stream = GameMvc.model().get(ItemContainer.class).contained.entrySet().stream();
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
    
    public ContainedItemsStream filterReachable(Position position) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        stream = stream.filter(entry -> map.passageMap.util.positionReachable(position, entry.getValue().entity.position, true));
        return this;
    }
}
