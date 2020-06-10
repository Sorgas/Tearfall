package stonering.game.model.system.item;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import stonering.entity.item.Item;
import stonering.entity.item.ItemGroupingKey;
import stonering.game.GameMvc;
import stonering.game.model.system.EntityStream;

/**
 * @author Alexander on 5/18/2020
 */
public class ItemsStream extends EntityStream<Item> {
    private ItemContainer container;

    public ItemsStream(Collection<Item> items) {
        super(items);
    }

    @Override
    protected ItemContainer container() {
        return container != null ? container : (container = GameMvc.model().get(ItemContainer.class));
    }

    public Map<ItemGroupingKey, List<Item>> groupByTypeAndMaterial() {
        return stream.collect(Collectors.groupingBy(ItemGroupingKey::new));
    }

    public Map<ItemGroupingKey, List<Item>> groupByTypeAndMaterial(int minimalGroupSize) {
        return groupByTypeAndMaterial().entrySet().stream()
                .filter(entry -> entry.getValue().size() >= minimalGroupSize) // filter items with sufficient number
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
