package stonering.entity.item.selectors;

import stonering.entity.item.Item;
import stonering.entity.item.ItemGroupingKey;
import stonering.game.model.system.item.ItemsStream;
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
     * Splits given items into groups by type and material.
     * Filters out groups with insufficient number of items.
     * Returns group with item, nearest to position.
     *
     * @param items items should fit to selector.
     */
    public List<Item> selectVariant(List<Item> items, int number, Position position) {
        Map<ItemGroupingKey, List<Item>> itemMap = new ItemsStream(items).groupByTypeAndMaterial(number);
        if (itemMap.isEmpty()) return new ArrayList<>(); // no group has enough items
        return itemMap.values().stream() // find group with nearest item
                .min(Comparator.comparingInt(list -> list.stream()
                        .map(item -> position.fastDistance(item.position)) // map to distance
                        .min(Comparator.comparingInt(Integer::intValue)).get())).get();
    }
}
