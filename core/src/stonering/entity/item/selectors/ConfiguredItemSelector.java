package stonering.entity.item.selectors;

import stonering.entity.item.Item;
import stonering.entity.item.ItemGroupingKey;
import stonering.util.geometry.Position;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Items selector which can select any combination of item types and materials.
 * However, only one item type/material can be used in buildings and crafting,
 * so this selector has an additional method to find appropriate items which number is enough.
 *
 * @author Alexander on 05.04.2020
 */
public class ConfiguredItemSelector extends ItemSelector {
    public final Map<String, Set<Integer>> map; // types to materials

    public ConfiguredItemSelector(Map<String, Set<Integer>> map) {
        this.map = map;
    }

    @Override
    public boolean checkItem(Item item) {
        return map.containsKey(item.type.name) && map.get(item.type.name).contains(item.material);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String type : map.keySet()) {
            s.append(type).append(map.get(type)).append('\n');
        }
        return s.toString();
    }

    /**
     * Selects some number of items with same type, nearest to given position.
     * Given items should fit selector.
     */
    public List<Item> selectVariant(List<Item> items, int number, Position position) {
        // group by type and material considering mixing
        // check group number
        Map<ItemGroupingKey, List<Item>> itemMap = items.stream()
                .collect(Collectors.groupingBy(ItemGroupingKey::new)) // group by type and material
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() >= number) // filter items with sufficient number
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if(itemMap.isEmpty()) return new ArrayList<>(); // no group has enough items
        // find group with nearest item
        List<Item> itemList = itemMap.values().stream()
                .min(Comparator.comparingInt(list -> list.stream()
                        .map(item -> position.fastDistance(item.position)) // map to distance
                        .min(Comparator.comparingInt(Integer::intValue)).get())).get();
        itemList.sort(Comparator.comparingInt(item -> position.fastDistance(item.position)));
        return itemList.subList(0, number); // return nearest items of nearest group
    }
}
